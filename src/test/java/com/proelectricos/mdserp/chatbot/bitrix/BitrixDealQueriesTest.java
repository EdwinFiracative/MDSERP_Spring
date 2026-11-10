package com.proelectricos.mdserp.chatbot.bitrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SuppressWarnings("unchecked")
class BitrixDealQueriesTest {

    private static final String WEBHOOK = "https://portal.bitrix24.es/rest/1/token/";

    private MockRestServiceServer server;
    private BitrixDealQueries queries;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(WEBHOOK);
        server = MockRestServiceServer.bindTo(builder).build();
        queries = new BitrixDealQueries(builder.build(), WEBHOOK);
    }

    private void esperaEmbudos() {
        responde("crm.dealcategory.list", """
                {"result":[{"ID":"1","NAME":"Celdas y Tableros"},{"ID":"3","NAME":"Ingeniería"},
                 {"ID":"16","NAME":"C de A Celdas y Tableros"},{"ID":"18","NAME":"Celdas y Tableros - No Construcción"}]}""");
        for (int id : BitrixDealQueries.EMBUDOS) {
            responde("crm.dealcategory.stage.list", """
                    {"result":[{"STATUS_ID":"C%1$d:NEW","NAME":"Diagnosticar y Calificar"},
                     {"STATUS_ID":"C%1$d:EXECUTING","NAME":"Comprometer"},
                     {"STATUS_ID":"C%1$d:WON","NAME":"Cerrado Ganado"},
                     {"STATUS_ID":"C%1$d:LOSE","NAME":"Cerrado Perdido"}]}""".formatted(id));
        }
    }

    private void responde(String metodo, String json) {
        server.expect(requestTo(WEBHOOK + metodo + ".json"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
    }

    private static final String TRES_NEGOCIOS = """
            {"result":[
              {"ID":"10","TITLE":"Tablero A","CATEGORY_ID":"1","STAGE_ID":"C1:WON","STAGE_SEMANTIC_ID":"S",
               "OPPORTUNITY":"1000.00","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-02T20:00:00+03:00","ASSIGNED_BY_ID":"51",
               "UF_CRM_1574869875":"124"},
              {"ID":"11","TITLE":"Tablero B","CATEGORY_ID":"1","STAGE_ID":"C1:LOSE","STAGE_SEMANTIC_ID":"F",
               "OPPORTUNITY":"500","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-03T20:00:00+03:00","ASSIGNED_BY_ID":"51"},
              {"ID":"12","TITLE":"Diseño","CATEGORY_ID":"3","STAGE_ID":"C3:NEW","STAGE_SEMANTIC_ID":"P",
               "OPPORTUNITY":"250.5","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-06T20:00:00+03:00","ASSIGNED_BY_ID":"21",
               "UF_CRM_1574869875":"124"}],
             "total":3}""";

    private static final String USUARIOS = """
            {"result":{"result":{"u51":[{"ID":"51","NAME":"Ana","LAST_NAME":"Pérez"}],
             "u21":[{"ID":"21","NAME":"Luis","LAST_NAME":"Gómez"}],
             "u124":[{"ID":"124","NAME":"Marta","LAST_NAME":"Ruiz"}]},"result_error":[]}}""";

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> lista(Map<String, Object> r, String clave) {
        return (List<Map<String, Object>>) r.get(clave);
    }

    @Test
    void reporteFiltraPorEtapaCambiadaEnHoraDeColombiaYAgrupa() {
        esperaEmbudos();
        server.expect(requestTo(WEBHOOK + "crm.deal.list.json"))
                .andExpect(content().json("""
                        {"filter":{"CATEGORY_ID":[1,18,3,16],">=MOVED_TIME":"2026-10-01T00:00-05:00",
                         "<MOVED_TIME":"2026-11-01T00:00-05:00"},"start":0}"""))
                .andRespond(withSuccess("""
                        {"result":[
                          {"ID":"10","TITLE":"Tablero A","CATEGORY_ID":"1","STAGE_ID":"C1:WON","STAGE_SEMANTIC_ID":"S",
                           "OPPORTUNITY":"1000.00","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-02T20:00:00+03:00",
                           "ASSIGNED_BY_ID":"51"},
                          {"ID":"11","TITLE":"Tablero B","CATEGORY_ID":"1","STAGE_ID":"C1:WON","STAGE_SEMANTIC_ID":"S",
                           "OPPORTUNITY":"500","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-03T20:00:00+03:00",
                           "ASSIGNED_BY_ID":"51"}],
                         "next":2,"total":3}""", MediaType.APPLICATION_JSON));
        server.expect(requestTo(WEBHOOK + "crm.deal.list.json"))
                .andExpect(jsonPath("$.start").value(2))
                .andRespond(withSuccess("""
                        {"result":[
                          {"ID":"12","TITLE":"Diseño","CATEGORY_ID":"3","STAGE_ID":"C3:NEW","STAGE_SEMANTIC_ID":"P",
                           "OPPORTUNITY":"250.5","CURRENCY_ID":"COP","MOVED_TIME":"2026-10-04T20:00:00+03:00",
                           "ASSIGNED_BY_ID":"21"}],
                         "total":3}""", MediaType.APPLICATION_JSON));
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, null, null, null, null, false);

        server.verify();
        assertThat((Map<String, Object>) r.get("totales"))
                .containsEntry("Negocios", 3)
                .containsEntry("Importe", new BigDecimal("1751"))
                .containsEntry("ImporteGanado", new BigDecimal("1500"))
                .containsEntry("ImporteEnProceso", new BigDecimal("251"));
        assertThat(r.get("truncado")).isEqualTo(false);
        assertThat(r).doesNotContainKey("aviso_responsables").doesNotContainKey("por_periodo");
        List<Map<String, Object>> porEtapa = lista(r, "por_embudo_y_etapa");
        assertThat(porEtapa).hasSize(2);
        assertThat(porEtapa.get(0)).containsEntry("Embudo", "Celdas y Tableros").containsEntry("Etapa", "Cerrado Ganado")
                .containsEntry("Negocios", 2).containsEntry("Importe", new BigDecimal("1500"));
        assertThat(porEtapa.get(1)).containsEntry("Embudo", "Ingeniería").containsEntry("Grupo", "En proceso");
        List<Map<String, Object>> porResponsable = lista(r, "por_responsable");
        assertThat(porResponsable).extracting(f -> f.get("Responsable")).containsExactly("Ana Pérez", "Luis Gómez");
        assertThat(porResponsable.get(0)).containsEntry("Negocios", 2).containsEntry("Ganados", 2);
    }

    @Test
    void reporteSemanalPorResponsable() {
        esperaEmbudos();
        responde("crm.deal.list", TRES_NEGOCIOS);
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, null, null, "semana", null, false);

        server.verify();
        assertThat(r.get("agrupado_por")).isEqualTo("semana");
        List<Map<String, Object>> periodos = lista(r, "por_periodo");
        assertThat(periodos).extracting(f -> f.get("Periodo"))
                .containsExactly("2026-10-01 a 2026-10-04", "2026-10-05 a 2026-10-11");
        assertThat(periodos.get(0)).containsEntry("Negocios", 2).containsEntry("ImporteGanado", new BigDecimal("1000"))
                .containsEntry("ImportePerdido", new BigDecimal("500"));
        assertThat(lista(r, "por_periodo_y_responsable"))
                .extracting(f -> f.get("Periodo") + " / " + f.get("Responsable"))
                .containsExactly("2026-10-01 a 2026-10-04 / Ana Pérez", "2026-10-05 a 2026-10-11 / Luis Gómez");
    }

    @Test
    void reporteMensualFiltradoPorNombreDeResponsable() {
        esperaEmbudos();
        responde("crm.deal.list", TRES_NEGOCIOS);
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 31),
                null, null, "luis gomez", null, "mes", null, false);

        server.verify();
        assertThat((Map<String, Object>) r.get("totales")).containsEntry("Negocios", 1);
        assertThat(lista(r, "por_periodo")).extracting(f -> f.get("Periodo")).containsExactly("2026-10");
        assertThat(lista(r, "por_responsable")).extracting(f -> f.get("Responsable")).containsExactly("Luis Gómez");
    }

    @Test
    void sinPermisoDeUsuariosMuestraIdYAvisa() {
        esperaEmbudos();
        responde("crm.deal.list", TRES_NEGOCIOS);
        responde("batch", """
                {"result":{"result":[],"result_error":{"u51":{"error":"insufficient_scope","error_description":""},
                 "u21":{"error":"insufficient_scope","error_description":""}}}}""");
        // Segunda consulta: mientras no haya permiso no se vuelve a pedir usuarios a Bitrix (no hay otro "batch")
        responde("crm.deal.list", TRES_NEGOCIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, null, null, null, null, false);

        assertThat(r.get("aviso_responsables")).isEqualTo(BitrixDealQueries.AVISO_SIN_PERMISO_USUARIOS);
        assertThat(lista(r, "por_responsable")).extracting(f -> f.get("Responsable")).containsExactly("ID 51", "ID 21");

        // Sin permiso tampoco se puede buscar un responsable por nombre
        assertThatThrownBy(() -> queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, "ana", null, null, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("permiso 'user'");
        server.verify();
    }

    @Test
    void etapaPorTextoSeTraduceAStageIdDelEmbudo() {
        esperaEmbudos();
        server.expect(requestTo(WEBHOOK + "crm.deal.list.json"))
                .andExpect(content().json("{\"filter\":{\"CATEGORY_ID\":[18],\"STAGE_ID\":[\"C18:WON\"]}}"))
                .andRespond(withSuccess("{\"result\":[],\"total\":0}", MediaType.APPLICATION_JSON));

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                18, "ganado", null, null, null, null, false);

        server.verify();
        assertThat((Map<String, Object>) r.get("totales")).containsEntry("Negocios", 0);
        assertThat(r.get("filtro_etapas")).isEqualTo(List.of("Cerrado Ganado"));
    }

    @Test
    void responsablePorIdSeFiltraEnBitrix() {
        esperaEmbudos();
        server.expect(requestTo(WEBHOOK + "crm.deal.list.json"))
                .andExpect(content().json("{\"filter\":{\"ASSIGNED_BY_ID\":\"51\"}}"))
                .andRespond(withSuccess("{\"result\":[],\"total\":0}", MediaType.APPLICATION_JSON));

        queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), null, null, "51", null,
                null, null, false);

        server.verify();
    }

    @Test
    void embudoFueraDeLosDeVentaSeRechaza() {
        esperaEmbudos();
        assertThatThrownBy(() -> queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                8, null, null, null, null, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Embudo no consultable");
    }

    @Test
    void consultarNegocioDevuelveProyectoImporteResponsableYFechaDeEtapa() {
        server.expect(requestTo(WEBHOOK + "crm.deal.get.json"))
                .andExpect(content().json("{\"id\":74506}"))
                .andRespond(withSuccess("""
                        {"result":{"ID":"74506","TITLE":"Tablero 3600A","UF_CRM_1571078216102":"Torre Filia",
                         "CATEGORY_ID":"1","STAGE_ID":"C1:EXECUTING","STAGE_SEMANTIC_ID":"P","OPPORTUNITY":"88101856.00",
                         "CURRENCY_ID":"COP","MOVED_TIME":"2026-11-06T20:50:53+03:00","COMPANY_ID":"15028",
                         "ASSIGNED_BY_ID":"51","DATE_CREATE":"2026-10-30T22:22:47+03:00",
                         "CLOSEDATE":"2026-11-06T03:00:00+03:00"}}""", MediaType.APPLICATION_JSON));
        esperaEmbudos();
        responde("crm.company.list", "{\"result\":[{\"ID\":\"15028\",\"TITLE\":\"FILIA SAS\"}]}");
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.consultarNegocio(74506);

        server.verify();
        assertThat(r).containsEntry("encontrado", true).containsEntry("embudo_de_venta_consultado", true)
                .doesNotContainKey("aviso_responsables");
        @SuppressWarnings("unchecked")
        Map<String, Object> negocio = (Map<String, Object>) r.get("negocio");
        assertThat(negocio)
                .containsEntry("NombreProyecto", "Torre Filia")
                .containsEntry("Embudo", "Celdas y Tableros")
                .containsEntry("Etapa", "Comprometer")
                .containsEntry("Importe", new BigDecimal("88101856"))
                .containsEntry("EtapaCambiadaEl", "2026-11-06 12:50")
                .containsEntry("Compania", "FILIA SAS")
                .containsEntry("Responsable", "Ana Pérez")
                .containsEntry("Presupuestador", "Sin presupuestador")
                .containsEntry("FechaCierre", "2026-11-06")
                .containsEntry("Enlace", "https://portal.bitrix24.es/crm/deal/details/74506/");
    }

    @Test
    void reportePorPresupuestadorAsignado() {
        esperaEmbudos();
        responde("crm.deal.list", TRES_NEGOCIOS);
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, null, "marta", "semana", "presupuestador", true);

        server.verify();
        assertThat((Map<String, Object>) r.get("totales")).containsEntry("Negocios", 2)
                .containsEntry("Importe", new BigDecimal("1251"));
        assertThat(lista(r, "por_presupuestador")).extracting(f -> f.get("Presupuestador"))
                .containsExactly("Marta Ruiz");
        assertThat(lista(r, "por_periodo_y_presupuestador"))
                .extracting(f -> f.get("Periodo") + " / " + f.get("Presupuestador"))
                .containsExactly("2026-10-01 a 2026-10-04 / Marta Ruiz", "2026-10-05 a 2026-10-11 / Marta Ruiz");
        assertThat(r).doesNotContainKey("por_periodo_y_responsable");
        assertThat(lista(r, "negocios")).allSatisfy(n -> assertThat(n).containsEntry("Presupuestador", "Marta Ruiz"));
    }

    @Test
    void negocioSinPresupuestadorSeAgrupaComoSinPresupuestador() {
        esperaEmbudos();
        responde("crm.deal.list", TRES_NEGOCIOS);
        responde("batch", USUARIOS);

        Map<String, Object> r = queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31),
                null, null, null, null, null, null, false);

        assertThat(lista(r, "por_presupuestador")).extracting(f -> f.get("Presupuestador") + "=" + f.get("Negocios"))
                .containsExactly("Marta Ruiz=2", "Sin presupuestador=1");
    }

    @Test
    void presupuestadorPorIdSeFiltraEnBitrix() {
        esperaEmbudos();
        server.expect(requestTo(WEBHOOK + "crm.deal.list.json"))
                .andExpect(content().json("{\"filter\":{\"UF_CRM_1574869875\":\"124\"}}"))
                .andRespond(withSuccess("{\"result\":[],\"total\":0}", MediaType.APPLICATION_JSON));

        queries.reporteNegocios(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), null, null, null, "124",
                null, null, false);

        server.verify();
    }

    @Test
    void negocioInexistente() {
        server.expect(requestTo(WEBHOOK + "crm.deal.get.json"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"\",\"error_description\":\"Not found\"}"));

        Map<String, Object> r = queries.consultarNegocio(999);

        assertThat(r).containsEntry("encontrado", false);
    }
}
