package com.proelectricos.mdserp.chatbot.bitrix;

import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.Normalizer;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Consultas de solo lectura a los negocios (deals) de Bitrix24 para el chatbot, con su propio webhook
 * (chatbot.bitrix.webhook-url). Solo se consultan los embudos de venta configurados (1, 18, 3 y 16).
 * La fecha de los reportes es "La etapa cambiada el" (MOVED_TIME), interpretada en hora de Colombia.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class BitrixDealQueries {

    /** Embudos de venta que consulta el bot, en el orden en que se muestran. */
    static final List<Integer> EMBUDOS = List.of(1, 18, 3, 16);
    static final ZoneId ZONA = ZoneId.of("America/Bogota");

    // Campo personalizado "Nombre del Proyecto" del negocio
    static final String CAMPO_NOMBRE_PROYECTO = "UF_CRM_1571078216102";
    private static final List<String> CAMPOS = List.of("ID", "TITLE", CAMPO_NOMBRE_PROYECTO, "CATEGORY_ID", "STAGE_ID",
            "STAGE_SEMANTIC_ID", "OPPORTUNITY", "CURRENCY_ID", "MOVED_TIME", "COMPANY_ID", "ASSIGNED_BY_ID",
            "DATE_CREATE", "CLOSEDATE");
    // Bitrix devuelve 50 negocios por página; el tope evita reportes de minutos (≈2 páginas por segundo)
    private static final int MAX_NEGOCIOS = 3000;
    private static final int MAX_DETALLE = 100;
    private static final Duration VIGENCIA_EMBUDOS = Duration.ofHours(6);
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Map<String, String> GRUPOS = Map.of("P", "En proceso", "S", "Ganado", "F", "Perdido");

    private final RestClient client;
    private final String portalUrl;
    private volatile Embudos embudos;

    @Autowired
    public BitrixDealQueries(ChatbotProperties properties) {
        this(cliente(properties.bitrix()), properties.bitrix().webhookUrl());
    }

    BitrixDealQueries(RestClient client, String webhookUrl) {
        this.client = client;
        this.portalUrl = portal(webhookUrl);
    }

    /** Datos principales de un negocio por su ID (nombre, proyecto, embudo, etapa, importe, fechas, compañía). */
    public Map<String, Object> consultarNegocio(long id) {
        Map<String, Object> respuesta = llamar("crm.deal.get", Map.of("id", id));
        Map<String, Object> resultado = new LinkedHashMap<>();
        if (respuesta.get("result") instanceof Map<?, ?> negocio) {
            @SuppressWarnings("unchecked")
            Map<String, Object> deal = (Map<String, Object>) negocio;
            Map<String, Object> fila = fila(deal);
            Object compania = deal.get("COMPANY_ID");
            fila.put("Compania", nombresCompanias(Set.of(String.valueOf(compania))).get(String.valueOf(compania)));
            fila.put("ResponsableId", deal.get("ASSIGNED_BY_ID"));
            fila.put("Creado", fechaHora(deal.get("DATE_CREATE")));
            fila.put("FechaCierre", fecha(deal.get("CLOSEDATE")));
            fila.put("Enlace", portalUrl + "crm/deal/details/" + id + "/");
            resultado.put("encontrado", true);
            resultado.put("embudo_de_venta_consultado", EMBUDOS.contains(entero(deal.get("CATEGORY_ID"))));
            resultado.put("negocio", fila);
            return resultado;
        }
        if (esNoEncontrado(respuesta)) {
            resultado.put("encontrado", false);
            resultado.put("mensaje", "No existe el negocio " + id + " en Bitrix24.");
            return resultado;
        }
        throw error("crm.deal.get", respuesta);
    }

    /**
     * Negocios cuya etapa cambió entre dos fechas (ambas incluidas, hora de Colombia), agrupados por embudo y etapa
     * con cantidad e importe. Opcionalmente filtra por un embudo y por etapa (texto del nombre, p. ej. "ganado").
     */
    public Map<String, Object> negociosPorEtapa(LocalDate desde, LocalDate hasta, Integer embudo, String etapa,
                                                boolean detalle) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la final");
        }
        if (embudo != null && !EMBUDOS.contains(embudo)) {
            throw new IllegalArgumentException("Embudo no consultable: " + embudo + ". Embudos disponibles: "
                    + embudos().descripcion());
        }
        Embudos catalogo = embudos();
        List<Integer> categorias = embudo == null ? EMBUDOS : List.of(embudo);

        Map<String, Object> filtro = new LinkedHashMap<>();
        filtro.put("CATEGORY_ID", categorias);
        filtro.put(">=MOVED_TIME", desde.atStartOfDay(ZONA).toOffsetDateTime().toString());
        filtro.put("<MOVED_TIME", hasta.plusDays(1).atStartOfDay(ZONA).toOffsetDateTime().toString());
        if (etapa != null && !etapa.isBlank()) {
            List<String> etapas = catalogo.etapasQueCoinciden(categorias, etapa);
            if (etapas.isEmpty()) {
                throw new IllegalArgumentException("Ninguna etapa coincide con \"" + etapa + "\". Etapas: "
                        + catalogo.nombresDeEtapas(categorias));
            }
            filtro.put("STAGE_ID", etapas);
        }

        List<Map<String, Object>> negocios = new ArrayList<>();
        boolean truncado = listar(filtro, negocios);

        // Resumen embudo -> etapa en el orden del embudo; los totales se calculan aquí, no en el modelo
        Map<String, Map<String, Object>> resumen = new LinkedHashMap<>();
        Map<Integer, BigDecimal> totalPorEmbudo = new LinkedHashMap<>();
        Map<Integer, Integer> cantidadPorEmbudo = new LinkedHashMap<>();
        Map<String, BigDecimal> totalPorGrupo = new LinkedHashMap<>();
        Set<String> monedas = new LinkedHashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        negocios.sort(Comparator.comparing((Map<String, Object> n) -> catalogo.orden(n.get("STAGE_ID"))));
        for (Map<String, Object> negocio : negocios) {
            int categoria = entero(negocio.get("CATEGORY_ID"));
            String etapaId = String.valueOf(negocio.get("STAGE_ID"));
            BigDecimal importe = decimal(negocio.get("OPPORTUNITY"));
            Map<String, Object> grupo = resumen.computeIfAbsent(categoria + "|" + etapaId, k -> {
                Map<String, Object> f = new LinkedHashMap<>();
                f.put("Embudo", catalogo.nombreEmbudo(categoria));
                f.put("Etapa", catalogo.nombreEtapa(etapaId));
                f.put("Grupo", GRUPOS.getOrDefault(String.valueOf(negocio.get("STAGE_SEMANTIC_ID")), ""));
                f.put("Negocios", 0);
                f.put("Importe", BigDecimal.ZERO);
                return f;
            });
            grupo.put("Negocios", (int) grupo.get("Negocios") + 1);
            grupo.put("Importe", ((BigDecimal) grupo.get("Importe")).add(importe));
            totalPorEmbudo.merge(categoria, importe, BigDecimal::add);
            cantidadPorEmbudo.merge(categoria, 1, Integer::sum);
            totalPorGrupo.merge(GRUPOS.getOrDefault(String.valueOf(negocio.get("STAGE_SEMANTIC_ID")), "Otro"),
                    importe, BigDecimal::add);
            if (negocio.get("CURRENCY_ID") != null) {
                monedas.add(String.valueOf(negocio.get("CURRENCY_ID")));
            }
            total = total.add(importe);
        }

        List<Map<String, Object>> porEmbudo = new ArrayList<>();
        for (Integer categoria : categorias) {
            Map<String, Object> f = new LinkedHashMap<>();
            f.put("EmbudoId", categoria);
            f.put("Embudo", catalogo.nombreEmbudo(categoria));
            f.put("Negocios", cantidadPorEmbudo.getOrDefault(categoria, 0));
            f.put("Importe", totalPorEmbudo.getOrDefault(categoria, BigDecimal.ZERO));
            porEmbudo.add(f);
        }

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("fecha_usada", "La etapa cambiada el (MOVED_TIME), hora de Colombia");
        resultado.put("desde", desde.toString());
        resultado.put("hasta", hasta.toString());
        resultado.put("filtro_etapas", filtro.containsKey("STAGE_ID")
                ? ((List<?>) filtro.get("STAGE_ID")).stream().map(e -> catalogo.nombreEtapa(String.valueOf(e))).distinct().toList()
                : "todas");
        resultado.put("total_negocios", negocios.size());
        resultado.put("importe_total", total);
        resultado.put("monedas", monedas);
        resultado.put("importe_por_grupo", totalPorGrupo);
        resultado.put("por_embudo", porEmbudo);
        resultado.put("por_embudo_y_etapa", new ArrayList<>(resumen.values()));
        resultado.put("truncado", truncado);
        if (truncado) {
            resultado.put("aviso", "Se alcanzó el máximo de " + MAX_NEGOCIOS + " negocios; acota el rango de fechas.");
        }
        if (detalle) {
            List<Map<String, Object>> recientes = negocios.stream()
                    .sorted(Comparator.comparing((Map<String, Object> n) -> instante(n.get("MOVED_TIME"))).reversed())
                    .limit(MAX_DETALLE)
                    .toList();
            Set<String> companias = new LinkedHashSet<>();
            recientes.forEach(n -> companias.add(String.valueOf(n.get("COMPANY_ID"))));
            Map<String, String> nombres = nombresCompanias(companias);
            List<Map<String, Object>> filas = new ArrayList<>();
            for (Map<String, Object> negocio : recientes) {
                Map<String, Object> f = fila(negocio);
                f.put("Compania", nombres.get(String.valueOf(negocio.get("COMPANY_ID"))));
                filas.add(f);
            }
            resultado.put("negocios", filas);
            resultado.put("negocios_truncados", negocios.size() > MAX_DETALLE);
        }
        return resultado;
    }

    // Recorre las páginas de crm.deal.list; devuelve true si se cortó por el tope
    private boolean listar(Map<String, Object> filtro, List<Map<String, Object>> destino) {
        Object inicio = 0;
        while (inicio != null) {
            Map<String, Object> cuerpo = new LinkedHashMap<>();
            cuerpo.put("filter", filtro);
            cuerpo.put("select", CAMPOS);
            cuerpo.put("order", Map.of("ID", "ASC"));
            cuerpo.put("start", inicio);
            Map<String, Object> respuesta = llamar("crm.deal.list", cuerpo);
            if (!(respuesta.get("result") instanceof List<?> pagina)) {
                throw error("crm.deal.list", respuesta);
            }
            for (Object negocio : pagina) {
                if (destino.size() >= MAX_NEGOCIOS) {
                    return true;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> deal = (Map<String, Object>) negocio;
                destino.add(deal);
            }
            inicio = respuesta.get("next");
        }
        return false;
    }

    private Map<String, Object> fila(Map<String, Object> deal) {
        Embudos catalogo = embudos();
        int categoria = entero(deal.get("CATEGORY_ID"));
        Map<String, Object> f = new LinkedHashMap<>();
        f.put("ID", deal.get("ID"));
        f.put("Nombre", deal.get("TITLE"));
        Object proyecto = deal.get(CAMPO_NOMBRE_PROYECTO);
        f.put("NombreProyecto", proyecto == null || String.valueOf(proyecto).isBlank() ? deal.get("TITLE") : proyecto);
        f.put("Embudo", catalogo.nombreEmbudo(categoria));
        f.put("Etapa", catalogo.nombreEtapa(String.valueOf(deal.get("STAGE_ID"))));
        f.put("Grupo", GRUPOS.getOrDefault(String.valueOf(deal.get("STAGE_SEMANTIC_ID")), ""));
        f.put("Importe", decimal(deal.get("OPPORTUNITY")));
        f.put("Moneda", deal.get("CURRENCY_ID"));
        f.put("EtapaCambiadaEl", fechaHora(deal.get("MOVED_TIME")));
        return f;
    }

    private Map<String, String> nombresCompanias(Set<String> ids) {
        List<String> validos = ids.stream().filter(id -> !id.isBlank() && !"0".equals(id) && !"null".equals(id)).toList();
        Map<String, String> nombres = new TreeMap<>();
        for (int i = 0; i < validos.size(); i += 50) {
            Map<String, Object> respuesta = llamar("crm.company.list", Map.of(
                    "filter", Map.of("ID", validos.subList(i, Math.min(i + 50, validos.size()))),
                    "select", List.of("ID", "TITLE")));
            if (respuesta.get("result") instanceof List<?> companias) {
                for (Object c : companias) {
                    Map<?, ?> compania = (Map<?, ?>) c;
                    nombres.put(String.valueOf(compania.get("ID")), String.valueOf(compania.get("TITLE")));
                }
            } else {
                // El nombre de la compañía es complementario: si falla, el reporte sigue sin él
                log.warn("Chatbot Bitrix: no se pudieron leer compañías: {}", respuesta);
            }
        }
        return nombres;
    }

    // Embudos y etapas cambian muy poco: se leen una vez y se refrescan cada pocas horas
    private Embudos embudos() {
        Embudos actual = embudos;
        if (actual == null || actual.leido.isBefore(Instant.now().minus(VIGENCIA_EMBUDOS))) {
            actual = leerEmbudos();
            embudos = actual;
        }
        return actual;
    }

    private Embudos leerEmbudos() {
        Map<String, Object> respuesta = llamar("crm.dealcategory.list", Map.of());
        if (!(respuesta.get("result") instanceof List<?> categorias)) {
            throw error("crm.dealcategory.list", respuesta);
        }
        Map<Integer, String> nombres = new LinkedHashMap<>();
        for (Object c : categorias) {
            Map<?, ?> categoria = (Map<?, ?>) c;
            nombres.put(entero(categoria.get("ID")), String.valueOf(categoria.get("NAME")));
        }
        Map<Integer, List<Etapa>> etapas = new LinkedHashMap<>();
        for (Integer id : EMBUDOS) {
            Map<String, Object> r = llamar("crm.dealcategory.stage.list", Map.of("id", id));
            if (!(r.get("result") instanceof List<?> lista)) {
                throw error("crm.dealcategory.stage.list", r);
            }
            List<Etapa> delEmbudo = new ArrayList<>();
            for (Object e : lista) {
                Map<?, ?> etapa = (Map<?, ?>) e;
                delEmbudo.add(new Etapa(String.valueOf(etapa.get("STATUS_ID")), String.valueOf(etapa.get("NAME"))));
            }
            etapas.put(id, delEmbudo);
        }
        return new Embudos(nombres, etapas, Instant.now());
    }

    // Bitrix responde 400 con {"error": ...} (p. ej. negocio inexistente): se lee el cuerpo sin lanzar excepción
    private Map<String, Object> llamar(String metodo, Map<String, Object> cuerpo) {
        try {
            Map<String, Object> respuesta = client.post()
                    .uri(metodo + ".json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .exchange((req, res) -> res.bodyTo(new ParameterizedTypeReference<Map<String, Object>>() {}));
            return respuesta == null ? Map.of() : respuesta;
        } catch (RestClientException e) {
            log.warn("Chatbot Bitrix: error llamando {}: {}", metodo, e.getMessage());
            throw new IllegalStateException("No se pudo consultar Bitrix24 (" + metodo + "): " + e.getMessage(), e);
        }
    }

    private static boolean esNoEncontrado(Map<String, Object> respuesta) {
        return String.valueOf(respuesta.get("error_description")).toLowerCase(Locale.ROOT).contains("not found");
    }

    private static IllegalStateException error(String metodo, Map<String, Object> respuesta) {
        return new IllegalStateException("Bitrix24 respondió con error en " + metodo + ": "
                + respuesta.getOrDefault("error_description", respuesta.get("error")));
    }

    private static RestClient cliente(ChatbotProperties.Bitrix bitrix) {
        if (bitrix == null || bitrix.webhookUrl() == null || bitrix.webhookUrl().isBlank()) {
            log.warn("Chatbot: chatbot.bitrix.webhook-url vacío; las consultas a Bitrix24 fallarán");
        }
        String url = bitrix == null || bitrix.webhookUrl() == null ? "" : bitrix.webhookUrl().trim();
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(5));
        fabrica.setReadTimeout(Duration.ofSeconds(30));
        return RestClient.builder()
                .baseUrl(url.endsWith("/") ? url : url + "/")
                .requestFactory(fabrica)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // https://portal.bitrix24.es/rest/1/token/ -> https://portal.bitrix24.es/
    private static String portal(String webhookUrl) {
        try {
            URI uri = URI.create(webhookUrl == null ? "" : webhookUrl.trim());
            return uri.getHost() == null ? "" : uri.getScheme() + "://" + uri.getAuthority() + "/";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    private static Instant instante(Object valor) {
        if (valor == null || String.valueOf(valor).isBlank()) {
            return Instant.EPOCH;
        }
        try {
            return OffsetDateTime.parse(String.valueOf(valor)).toInstant();
        } catch (DateTimeParseException e) {
            return Instant.EPOCH;
        }
    }

    // Bitrix devuelve las fechas con la zona del portal; se muestran en hora de Colombia
    static String fechaHora(Object valor) {
        if (valor == null || String.valueOf(valor).isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(String.valueOf(valor)).atZoneSameInstant(ZONA).format(FECHA_HORA);
        } catch (DateTimeParseException e) {
            return String.valueOf(valor);
        }
    }

    // Campos de solo fecha (CLOSEDATE) llegan como medianoche UTC con la zona del portal: se toma el día UTC
    private static String fecha(Object valor) {
        if (valor == null || String.valueOf(valor).isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(String.valueOf(valor)).atZoneSameInstant(ZoneId.of("UTC")).toLocalDate().toString();
        } catch (DateTimeParseException e) {
            return String.valueOf(valor);
        }
    }

    private static BigDecimal decimal(Object valor) {
        if (valor == null || String.valueOf(valor).isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            // Pesos enteros (sin notación científica al serializar)
            return new BigDecimal(String.valueOf(valor)).setScale(0, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private static int entero(Object valor) {
        try {
            return Integer.parseInt(String.valueOf(valor).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).trim();
    }

    private record Etapa(String id, String nombre) {
    }

    private record Embudos(Map<Integer, String> nombres, Map<Integer, List<Etapa>> etapas, Instant leido) {

        String nombreEmbudo(int id) {
            return nombres.getOrDefault(id, "Embudo " + id);
        }

        String nombreEtapa(String etapaId) {
            return etapas.values().stream().flatMap(List::stream)
                    .filter(e -> e.id().equals(etapaId)).map(Etapa::nombre).findFirst().orElse(etapaId);
        }

        // Posición de la etapa para ordenar el resumen: embudo en el orden de EMBUDOS y luego etapa en el del embudo
        int orden(Object etapaId) {
            int posicion = 0;
            for (Integer embudo : EMBUDOS) {
                for (Etapa e : etapas.getOrDefault(embudo, List.of())) {
                    if (e.id().equals(String.valueOf(etapaId))) {
                        return posicion;
                    }
                    posicion++;
                }
            }
            return Integer.MAX_VALUE;
        }

        List<String> etapasQueCoinciden(List<Integer> categorias, String texto) {
            String buscado = normalizar(texto);
            return categorias.stream()
                    .flatMap(c -> etapas.getOrDefault(c, List.of()).stream())
                    .filter(e -> normalizar(e.nombre()).contains(buscado) || e.id().equalsIgnoreCase(texto.trim()))
                    .map(Etapa::id)
                    .toList();
        }

        String nombresDeEtapas(List<Integer> categorias) {
            return String.join(", ", categorias.stream()
                    .flatMap(c -> etapas.getOrDefault(c, List.of()).stream())
                    .map(Etapa::nombre)
                    .distinct()
                    .toList());
        }

        String descripcion() {
            return String.join(", ", EMBUDOS.stream().map(id -> id + " = " + nombreEmbudo(id)).toList());
        }
    }
}
