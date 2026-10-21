package com.proelectricos.mdserp.erpdb.order.biconnector;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Tabla expuesta por el conector BI: codigo y titulo (urlTableList), columnas (urlTableDescription)
 * y carga de filas por rango de fecha de pedido (urlData).
 */
public record BiTable<R>(String code, String title, List<Column<R>> columns,
                         BiFunction<LocalDate, LocalDate, List<R>> loader) {

    /** Tipos de columna que admite Bitrix24 (los demas los trata como string). */
    public enum Type {
        INT, STRING, DOUBLE, DATE;

        public String code() {
            return name().toLowerCase();
        }
    }

    public record Column<R>(String code, String name, Type type, Function<R, Object> value) {
    }

    public Map<String, Column<R>> columnsByCode() {
        return columns.stream().collect(Collectors.toMap(Column::code, Function.identity()));
    }

    public Set<String> dateColumnCodes() {
        return columns.stream().filter(column -> column.type() == Type.DATE).map(Column::code).collect(Collectors.toSet());
    }
}
