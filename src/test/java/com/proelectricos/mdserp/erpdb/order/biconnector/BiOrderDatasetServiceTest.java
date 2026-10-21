package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderHeaderService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BiOrderDatasetServiceTest {

    // Muestra anonimizada de GET /api/orderheader (anexo A de INFORME_BITRIX_BI_CONNECTOR.md)
    static List<OrderHeaderDto> sample() throws IOException {
        try (InputStream json = BiOrderDatasetServiceTest.class.getResourceAsStream("/biconnector/orderheader-sample.json")) {
            return new ObjectMapper().findAndRegisterModules().readValue(json, new TypeReference<>() {
            });
        }
    }

    @Test
    void aplanaLaMuestraEnUnaLineaYUnaNota() throws IOException {
        OrderHeaderDto header = sample().get(0);

        List<BiOrderLineRow> lines = BiOrderDatasetService.toLineRows(header);
        List<BiOrderNoteRow> notes = BiOrderDatasetService.toNoteRows(header);

        assertThat(lines).containsExactly(new BiOrderLineRow(
                239499L, "49745-1", 102339L, 49745, LocalDate.of(2026, 10, 19),
                "CREDITO  VS", "CREDITO VS", "OBRA EJEMPLO - TORRE 1",
                982L, "01", "SOACHA (CUNDINAMARCA", "CL 00 # 00-00 (anonimizada)",
                2836L, "900000000", "CLIENTE EJEMPLO S.A.S.",
                184L, "27197", "VENDEDOR EJEMPLO",
                1, 164973L, "32114974501", "TABLERO PALNTA ELECTRICA UN 208/120V - IN 2000A - ICC 35KA",
                "TADI.T.2000A.65KA.IP43.IN.CR", "CONJUNTO",
                1154L, "celdas y tableros",
                1, new BigDecimal("17661953.0000"), new BigDecimal("17661953.0000"), LocalDate.of(2026, 11, 23),
                14079L, "PROYECTO EJEMPLO", 2L, "Aprobado"));
        assertThat(notes).containsExactly(
                new BiOrderNoteRow(49745, LocalDate.of(2026, 10, 19), 59857L, 1, "NOTA DE EJEMPLO 1"));
    }

    @Test
    void lineaSinLineaDeNegocioNiProyectoQuedaConNulos() throws IOException {
        OrderHeaderDto header = sample().get(0);
        header.getOrderReference().get(0).setOrderReferBusinessLine(null);
        header.getOrderReference().get(0).setOrderReferProject(null);

        BiOrderLineRow line = BiOrderDatasetService.toLineRows(header).get(0);

        assertThat(line.lineaNegocioId()).isNull();
        assertThat(line.lineaNegocio()).isNull();
        assertThat(line.proyectoId()).isNull();
        assertThat(line.proyecto()).isNull();
    }

    // Tantas filas como orderReference devuelve el servicio de /api/orderheader, ordenadas por pedido y posicion
    @Test
    void devuelveUnaFilaPorCadaLineaDelEndpointExistente() throws IOException {
        OrderHeaderDto first = sample().get(0);
        OrderHeaderDto second = sample().get(0);
        second.setOrderHeaderNumber(49744);
        second.getOrderReference().add(sample().get(0).getOrderReference().get(0));
        second.getOrderReference().get(1).setOrderReferPosition(2);
        LocalDate day = LocalDate.of(2026, 10, 19);
        OrderHeaderService orderHeaderService = mock(OrderHeaderService.class);
        when(orderHeaderService.findByDateRange(day, day, null)).thenReturn(List.of(first, second));

        List<BiOrderLineRow> lines = new BiOrderDatasetService(orderHeaderService).findLines(day, day);

        assertThat(lines).hasSize(first.getOrderReference().size() + second.getOrderReference().size());
        assertThat(lines).extracting(BiOrderLineRow::claveLinea).containsExactly("49744-1", "49744-2", "49745-1");
    }

    @Test
    void normalizaCondicionDePago() {
        assertThat(BiOrderDatasetService.normalizePaymentCondition("  credito   vs ")).isEqualTo("CREDITO VS");
        assertThat(BiOrderDatasetService.normalizePaymentCondition(null)).isNull();
    }
}
