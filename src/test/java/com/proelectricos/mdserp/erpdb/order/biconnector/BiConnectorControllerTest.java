package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BiConnectorControllerTest {

    private static final String KEY = "clave-de-prueba-0123456789abcdef";
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 20);

    private BiOrderDatasetService datasetService;
    private BiMarginDatasetService marginService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        datasetService = mock(BiOrderDatasetService.class);
        marginService = mock(BiMarginDatasetService.class);
        Clock clock = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        BiConnectorController controller = new BiConnectorController(
                new BiConnectorTables(datasetService, marginService), new ObjectMapper(), 90, clock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).addFilters(new BiConnectorKeyFilter(KEY)).build();
    }

    // Bitrix24 envia form-urlencoded con la clave en connection[key]
    private static MockHttpServletRequestBuilder call(String endpoint, String key) {
        MockHttpServletRequestBuilder request = post("/bi-connector/" + endpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        return key == null ? request : request.param("connection[key]", key);
    }

    private static BiOrderLineRow line(int orderNumber, int position, LocalDate date, long statusId) throws IOException {
        BiOrderLineRow row = BiOrderDatasetService.toLineRows(BiOrderDatasetServiceTest.sample().get(0)).get(0);
        return new BiOrderLineRow(row.idLinea(), orderNumber + "-" + position, row.idPedido(), orderNumber, date,
                row.condicionPago(), row.condicionPagoNorm(), row.descripcionPedido(), row.sedeId(), row.sedeCodigo(),
                row.sedeCiudad(), row.sedeDireccion(), row.sedeZona(), row.clienteId(), row.clienteNit(), row.clienteNombre(),
                row.vendedorId(), row.vendedorCodigo(), row.vendedorCedula(), row.vendedorNombre(), position, row.productoId(),
                row.productoCodigo(), row.productoNombre(), row.productoDescripcion(), row.unidadMedida(),
                row.lineaNegocioId(), row.lineaNegocio(), row.cantidad(), row.precioUnitario(), row.valorTotal(),
                row.fechaEntrega(), row.proyectoId(), row.proyecto(), statusId, row.estado());
    }

    @Test
    void rechazaClaveInvalidaOAusente() throws Exception {
        mockMvc.perform(call("check", "otra-clave")).andExpect(status().isUnauthorized());
        mockMvc.perform(call("data", null).param("table", "pedidos_lineas")).andExpect(status().isUnauthorized());
    }

    @Test
    void sinClaveConfiguradaRechazaTodo() throws Exception {
        MockMvc sinClave = MockMvcBuilders.standaloneSetup(new BiConnectorController(
                        new BiConnectorTables(datasetService, marginService), new ObjectMapper(), 90, Clock.systemUTC()))
                .addFilters(new BiConnectorKeyFilter("")).build();

        sinClave.perform(call("check", "")).andExpect(status().isUnauthorized());
    }

    @Test
    void checkRespondeOk() throws Exception {
        mockMvc.perform(call("check", KEY)).andExpect(status().isOk()).andExpect(content().string("OK"));
    }

    @Test
    void tableListDevuelveLasTresTablas() throws Exception {
        mockMvc.perform(call("table_list", KEY).param("searchString", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].code").value("pedidos_lineas"))
                .andExpect(jsonPath("$[1].code").value("pedidos_notas"))
                .andExpect(jsonPath("$[2].code").value("pedidos_margen"));

        mockMvc.perform(call("table_list", KEY).param("searchString", "notas"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("pedidos_notas"));
    }

    @Test
    void tableDescriptionDevuelveLasColumnas() throws Exception {
        mockMvc.perform(call("table_description", KEY).param("table", "pedidos_lineas"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(36))
                .andExpect(jsonPath("$[0].code").value("id_linea"))
                .andExpect(jsonPath("$[0].type").value("int"))
                .andExpect(jsonPath("$[4].code").value("fecha_pedido"))
                .andExpect(jsonPath("$[4].type").value("date"))
                .andExpect(jsonPath("$[12].code").value("sede_zona"))
                .andExpect(jsonPath("$[12].type").value("string"))
                .andExpect(jsonPath("$[30].code").value("valor_total"))
                .andExpect(jsonPath("$[30].type").value("double"));

        mockMvc.perform(call("table_description", KEY).param("table", "pedidos_notas"))
                .andExpect(jsonPath("$[*].code").value(org.hamcrest.Matchers.contains(
                        "numero_pedido", "fecha_pedido", "id_nota", "posicion", "texto")));

        mockMvc.perform(call("table_description", KEY).param("table", "pedidos_margen"))
                .andExpect(jsonPath("$.length()").value(42))
                .andExpect(jsonPath("$[4].code").value("fecha_pedido"))
                .andExpect(jsonPath("$[4].type").value("date"))
                .andExpect(jsonPath("$[25].code").value("margen_real_pct"))
                .andExpect(jsonPath("$[25].type").value("double"));

        mockMvc.perform(call("table_description", KEY).param("table", "no_existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void dataConRangoDevuelveCabeceraYFilasYRespetaLimit() throws Exception {
        LocalDate day = LocalDate.of(2026, 10, 19);
        when(datasetService.findLines(day, day)).thenReturn(List.of(
                line(49744, 1, day, 2), line(49745, 1, day, 1), line(49745, 2, day, 2)));

        mockMvc.perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[>=fecha_pedido]", "2026-10-19")
                        .param("filter[<=fecha_pedido]", "19.10.2026")
                        .param("select[]", "clave_linea", "valor_total", "fecha_pedido")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[[\"clave_linea\",\"valor_total\",\"fecha_pedido\"],"
                        + "[\"49744-1\",17661953.0000,\"2026-10-19\"],[\"49745-1\",17661953.0000,\"2026-10-19\"]]"));
    }

    @Test
    void dataAplicaElFiltroSobreLasFilas() throws Exception {
        LocalDate day = LocalDate.of(2026, 10, 19);
        when(datasetService.findLines(day, day)).thenReturn(List.of(
                line(49744, 1, day, 2), line(49745, 1, day, 1), line(49745, 2, day, 2)));

        mockMvc.perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[=fecha_pedido]", "2026-10-19")
                        .param("filter[!estado_id]", "1")
                        .param("select[]", "clave_linea"))
                .andExpect(content().json("[[\"clave_linea\"],[\"49744-1\"],[\"49745-2\"]]"));
    }

    // Una fecha en formato desconocido no debe vaciar el resultado: la condicion se ignora
    @Test
    void dataIgnoraLasCondicionesDeFechaNoReconocidas() throws Exception {
        LocalDate day = LocalDate.of(2026, 10, 19);
        when(datasetService.findLines(day, day)).thenReturn(List.of(line(49744, 1, day, 2), line(49745, 1, day, 1)));

        mockMvc.perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[=fecha_pedido]", "2026-10-19")
                        .param("filter[>=fecha_pedido]", "1760832000")
                        .param("filter[<fecha_entrega]", "10/20/2026")
                        .param("filter[!estado_id]", "1")
                        .param("select[]", "clave_linea"))
                .andExpect(content().json("[[\"clave_linea\"],[\"49744-1\"]]"));
    }

    // BI_JSON_PARSING_ERROR "Count of returned BI cols must be equal to the count of requested cols"
    @Test
    void dataConCamposEnMayusculasDevuelveSoloLosPedidos() throws Exception {
        LocalDate day = LocalDate.of(2026, 10, 19);
        when(datasetService.findLines(day, day)).thenReturn(List.of(line(49744, 1, day, 2)));

        mockMvc.perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[>=FECHA_PEDIDO]", "2026-10-19")
                        .param("filter[<=FECHA_PEDIDO]", "2026-10-19")
                        .param("select[]", "CLAVE_LINEA", "VALOR_TOTAL"))
                .andExpect(content().json("[[\"clave_linea\",\"valor_total\"],[\"49744-1\",17661953.0000]]"));
    }

    @Test
    void dataSinSelectUsaLasColumnasDeMapFields() throws Exception {
        LocalDate day = LocalDate.of(2026, 10, 19);
        when(datasetService.findLines(day, day)).thenReturn(List.of(line(49744, 1, day, 2)));

        mockMvc.perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[=fecha_pedido]", "2026-10-19")
                        .param("mapFields[clave_linea]", "CLAVE_LINEA")
                        .param("mapFields[no_existe]", "NO_EXISTE"))
                .andExpect(content().json("[[\"clave_linea\",\"no_existe\"],[\"49744-1\",null]]"));
    }

    @Test
    void dataSinRangoUsaLaVentanaPorDefectoPorMeses() throws Exception {
        when(datasetService.findNotes(any(), any())).thenReturn(List.of());

        mockMvc.perform(call("data", KEY).param("table", "pedidos_notas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[[\"numero_pedido\",\"fecha_pedido\",\"id_nota\",\"posicion\",\"texto\"]]"));

        // 90 dias hasta hoy (2026-07-22 a 2026-10-20), consultados por mes
        verify(datasetService).findNotes(LocalDate.of(2026, 7, 22), LocalDate.of(2026, 7, 31));
        verify(datasetService).findNotes(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31));
        verify(datasetService).findNotes(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        verify(datasetService).findNotes(LocalDate.of(2026, 10, 1), TODAY);
    }

    private MockMvc conFechaMinima(LocalDate minDate) {
        Clock clock = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        return MockMvcBuilders.standaloneSetup(new BiConnectorController(
                        new BiConnectorTables(datasetService, marginService), new ObjectMapper(), 90, minDate, clock))
                .addFilters(new BiConnectorKeyFilter(KEY)).build();
    }

    // Sin rango en el filtro: desde la fecha minima hasta hoy (no la ventana de 90 dias), por meses
    @Test
    void dataSinRangoConFechaMinimaEnviaDesdeEsaFecha() throws Exception {
        when(marginService.findLines(any(), any())).thenReturn(List.of());

        conFechaMinima(LocalDate.of(2026, 1, 1)).perform(call("data", KEY).param("table", "pedidos_margen"))
                .andExpect(status().isOk());

        verify(marginService).findLines(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));
        verify(marginService).findLines(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));
        verify(marginService).findLines(LocalDate.of(2026, 10, 1), TODAY);
        verify(marginService, org.mockito.Mockito.times(10)).findLines(any(), any());
    }

    // Filtro de periodo de un tablero de BI Builder (portal en espanol), tal como llega en produccion:
    // dia/mes/anio con hora y "<" exclusivo. Antes se ignoraba y se devolvia todo 2026 para cualquier periodo
    @Test
    void dataConFechasDelTableroDiaMesAnioConHoraFiltraElPeriodo() throws Exception {
        when(marginService.findLines(any(), any())).thenReturn(List.of());

        conFechaMinima(LocalDate.of(2026, 1, 1)).perform(call("data", KEY).param("table", "pedidos_margen")
                        .param("filter[>=FECHA_PEDIDO]", "01/10/2026 12:00:00 am")
                        .param("filter[<FECHA_PEDIDO]", "02/11/2026 12:00:00 am"))
                .andExpect(status().isOk());

        // Del 1 de octubre al 1 de noviembre (el "<" 2 de noviembre lo deja fuera), por meses
        verify(marginService).findLines(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));
        verify(marginService).findLines(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 1));
        verify(marginService, org.mockito.Mockito.times(2)).findLines(any(), any());
    }

    @Test
    void parseDateAceptaDiaMesAnioConYSinHora() {
        assertThat(BiConnectorFilter.parseDate("01/11/2026 12:00:00 am")).isEqualTo(LocalDate.of(2026, 11, 1));
        assertThat(BiConnectorFilter.parseDate("02/12/2026")).isEqualTo(LocalDate.of(2026, 12, 2));
        assertThat(BiConnectorFilter.parseDate("1/4/2026 12:00:00 AM")).isEqualTo(LocalDate.of(2026, 4, 1));
        assertThat(BiConnectorFilter.parseDate("31/02/2026")).isNull();
    }

    // Un rango que empieza antes de la fecha minima se recorta: nunca se envian pedidos de 2025
    @Test
    void dataConRangoAnteriorSeRecortaALaFechaMinima() throws Exception {
        when(datasetService.findLines(any(), any())).thenReturn(List.of());

        conFechaMinima(LocalDate.of(2026, 1, 1)).perform(call("data", KEY).param("table", "pedidos_lineas")
                        .param("filter[>=fecha_pedido]", "2025-11-01")
                        .param("filter[<=fecha_pedido]", "2026-01-15"))
                .andExpect(status().isOk());

        verify(datasetService).findLines(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 15));
        verify(datasetService, org.mockito.Mockito.times(1)).findLines(any(), any());
    }
}
