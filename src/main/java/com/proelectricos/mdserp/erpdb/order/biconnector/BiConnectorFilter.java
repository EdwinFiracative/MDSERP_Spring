package com.proelectricos.mdserp.erpdb.order.biconnector;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Filtro del parametro "filter" de urlData. La documentacion de Bitrix24 solo dice "clave = campo,
 * valor = condicion"; se sigue la convencion de filtros de Bitrix (la misma del ejemplo oficial del SDK):
 * prefijo de operador en la clave (">=", "<=", ">", "<", "!=", "<>", "!", "=", "%"/"~" para contiene,
 * "@"/"!@" para listas), valor lista = IN, "LOGIC" = AND/OR y subgrupos anidados.
 * Los campos desconocidos se ignoran.
 */
@Slf4j
final class BiConnectorFilter {

    private static final List<String> OPERATORS = List.of(">=", "<=", "!=", "<>", "!@", ">", "<", "!", "=", "%", "~", "@");
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Pattern ISO_DATE = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}.*");
    private static final Pattern DMY_DATE = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}.*");

    private sealed interface Node permits Group, Condition {
    }

    private record Group(boolean or, List<Node> nodes) implements Node {
    }

    private record Condition(String field, String operator, List<String> values) implements Node {
    }

    /** Limites de fecha deducidos del filtro; null = sin limite por ese lado. */
    record DateRange(LocalDate from, LocalDate to) {
    }

    private final Group root;

    private BiConnectorFilter(Group root) {
        this.root = root;
    }

    static BiConnectorFilter parse(Object filter) {
        return new BiConnectorFilter(filter instanceof Map<?, ?> map ? group(map) : new Group(false, List.of()));
    }

    /**
     * Quita (y registra) las condiciones sobre columnas de fecha cuyo valor no se puede interpretar: al
     * compararlas descartarian todas las filas sin avisar. "Contiene" se deja, compara como texto.
     */
    BiConnectorFilter withoutInvalidDates(Set<String> dateColumns) {
        return new BiConnectorFilter(withoutInvalidDates(root, dateColumns));
    }

    private static Group withoutInvalidDates(Group group, Set<String> dateColumns) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : group.nodes()) {
            if (node instanceof Group child) {
                nodes.add(withoutInvalidDates(child, dateColumns));
            } else if (node instanceof Condition condition && dateColumns.contains(condition.field())
                    && !condition.operator().equals("%") && !condition.operator().equals("~")
                    && condition.values().stream().anyMatch(value -> parseDate(value) == null)) {
                log.warn("Conector BI: se ignora el filtro {}{} con fecha no reconocida {}",
                        condition.operator(), condition.field(), condition.values());
            } else {
                nodes.add(node);
            }
        }
        return new Group(group.or(), nodes);
    }

    /**
     * @param columns columnas de la tabla (las condiciones sobre otros campos se ignoran)
     * @param valueOf valor de la fila por codigo de columna
     */
    boolean test(Set<String> columns, Function<String, Object> valueOf) {
        return matches(root, columns, valueOf);
    }

    /**
     * Rango de la columna de fecha segun las condiciones del nivel superior (solo si es AND: con OR no se
     * puede acotar). Sirve para consultar a la base solo ese rango; el filtro completo se aplica despues.
     */
    DateRange dateRange(String field) {
        LocalDate from = null;
        LocalDate to = null;
        if (root.or()) {
            return new DateRange(null, null);
        }
        for (Node node : root.nodes()) {
            if (!(node instanceof Condition condition) || !condition.field().equals(field)) {
                continue;
            }
            List<LocalDate> dates = condition.values().stream().map(BiConnectorFilter::parseDate).toList();
            if (dates.isEmpty() || dates.contains(null)) {
                continue;
            }
            LocalDate min = dates.stream().min(LocalDate::compareTo).orElseThrow();
            LocalDate max = dates.stream().max(LocalDate::compareTo).orElseThrow();
            switch (condition.operator()) {
                case ">=" -> from = later(from, min);
                case ">" -> from = later(from, min.plusDays(1));
                case "<=" -> to = earlier(to, max);
                case "<" -> to = earlier(to, max.minusDays(1));
                case "=", "@" -> {
                    from = later(from, min);
                    to = earlier(to, max);
                }
                default -> {
                    // negaciones y "contiene" no acotan el rango
                }
            }
        }
        return new DateRange(from, to);
    }

    private static Group group(Map<?, ?> map) {
        boolean or = false;
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            if (key.equalsIgnoreCase("LOGIC")) {
                or = "OR".equalsIgnoreCase(String.valueOf(value).strip());
            } else if (value instanceof Map<?, ?> child && isGroup(child)) {
                nodes.add(group(child));
            } else {
                String operator = OPERATORS.stream().filter(key::startsWith).findFirst().orElse("=");
                // Bitrix24 puede usar el nombre de campo en mayusculas de table.add: los codigos van en minusculas
                String field = (key.startsWith(operator) ? key.substring(operator.length()) : key).toLowerCase(Locale.ROOT);
                nodes.add(new Condition(field, operator, FormParams.values(value)));
            }
        }
        return new Group(or, nodes);
    }

    // Un mapa es lista de valores (IN) si todas sus claves son indices y sus valores son texto
    private static boolean isGroup(Map<?, ?> map) {
        return !map.entrySet().stream().allMatch(entry -> String.valueOf(entry.getKey()).matches("\\d+")
                && entry.getValue() instanceof String);
    }

    private static boolean matches(Node node, Set<String> columns, Function<String, Object> valueOf) {
        if (node instanceof Group group) {
            if (group.nodes().isEmpty()) {
                return true;
            }
            return group.or()
                    ? group.nodes().stream().anyMatch(child -> matches(child, columns, valueOf))
                    : group.nodes().stream().allMatch(child -> matches(child, columns, valueOf));
        }
        Condition condition = (Condition) node;
        if (!columns.contains(condition.field())) {
            return true;
        }
        Object actual = valueOf.apply(condition.field());
        return switch (condition.operator()) {
            case "!=", "<>", "!", "!@" -> condition.values().stream().noneMatch(value -> compare(actual, value) == 0);
            case "=", "@" -> condition.values().stream().anyMatch(value -> compare(actual, value) == 0);
            case "%", "~" -> condition.values().stream().anyMatch(value -> contains(actual, value));
            default -> condition.values().stream().allMatch(value -> {
                Integer result = compare(actual, value);
                return result != null && switch (condition.operator()) {
                    case ">=" -> result >= 0;
                    case ">" -> result > 0;
                    case "<=" -> result <= 0;
                    default -> result < 0;
                };
            });
        };
    }

    // null si no se pueden comparar (valor nulo o filtro con formato invalido)
    private static Integer compare(Object actual, String expected) {
        if (actual == null || expected == null) {
            return null;
        }
        if (actual instanceof LocalDate date) {
            LocalDate other = parseDate(expected);
            return other == null ? null : date.compareTo(other);
        }
        if (actual instanceof Number number) {
            try {
                return new BigDecimal(number.toString()).compareTo(new BigDecimal(expected.strip()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return actual.toString().compareTo(expected);
    }

    // "%" en el valor es comodin; sin "%" busca el texto en cualquier parte (sin distinguir mayusculas)
    private static boolean contains(Object actual, String expected) {
        if (actual == null || expected == null) {
            return false;
        }
        String text = actual.toString().toLowerCase(Locale.ROOT);
        String pattern = expected.toLowerCase(Locale.ROOT);
        if (!pattern.contains("%")) {
            return text.contains(pattern);
        }
        StringBuilder regex = new StringBuilder();
        for (String part : pattern.split("%", -1)) {
            regex.append(Pattern.quote(part)).append(".*");
        }
        regex.setLength(regex.length() - 2);
        return text.matches(regex.toString());
    }

    // Admite yyyy-MM-dd, dd.MM.yyyy (con hora opcional, que se descarta) e ISO 8601 con zona
    static LocalDate parseDate(String value) {
        if (value == null) {
            return null;
        }
        String text = value.strip();
        try {
            if (ISO_DATE.matcher(text).matches()) {
                return text.length() > 10 && text.contains("T") && (text.endsWith("Z") || text.matches(".*[+-]\\d{2}:\\d{2}$"))
                        ? OffsetDateTime.parse(text).toLocalDate()
                        : LocalDate.parse(text.substring(0, 10));
            }
            if (DMY_DATE.matcher(text).matches()) {
                return LocalDate.parse(text.substring(0, 10), DMY);
            }
        } catch (DateTimeParseException e) {
            return null;
        }
        return null;
    }

    private static LocalDate later(LocalDate current, LocalDate candidate) {
        return current == null || candidate.isAfter(current) ? candidate : current;
    }

    private static LocalDate earlier(LocalDate current, LocalDate candidate) {
        return current == null || candidate.isBefore(current) ? candidate : current;
    }
}
