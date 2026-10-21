package com.proelectricos.mdserp.erpdb.order.biconnector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bitrix24 llama al conector con cuerpo application/x-www-form-urlencoded y objetos anidados en
 * notacion de corchetes ("connection[key]=x", "select[]=a", "filter[>=fecha_pedido]=2026-01-01").
 * Esto los convierte en mapas anidados; los "[]" se numeran en orden de llegada.
 */
final class FormParams {

    private FormParams() {
    }

    static Map<String, Object> parse(Map<String, String[]> params) {
        Map<String, Object> root = new LinkedHashMap<>();
        params.forEach((name, values) -> {
            List<String> path = path(name);
            // "select=a&select=b" sin corchetes tambien es una lista
            if (values.length > 1 && !path.get(path.size() - 1).isEmpty()) {
                path.add("");
            }
            for (String value : values) {
                put(root, path, value);
            }
        });
        return root;
    }

    // Valores de un nodo: el texto si es simple, los textos en orden si es una lista/mapa
    static List<String> values(Object node) {
        if (node instanceof String value) {
            return List.of(value);
        }
        if (node instanceof Map<?, ?> map) {
            return map.values().stream().filter(String.class::isInstance).map(String.class::cast).toList();
        }
        return List.of();
    }

    private static List<String> path(String name) {
        List<String> path = new ArrayList<>();
        int open = name.indexOf('[');
        if (open < 0 || !name.endsWith("]")) {
            path.add(name);
            return path;
        }
        path.add(name.substring(0, open));
        while (open >= 0) {
            int close = name.indexOf(']', open);
            if (close < 0) {
                break;
            }
            path.add(name.substring(open + 1, close));
            open = name.indexOf('[', close);
        }
        return path;
    }

    @SuppressWarnings("unchecked")
    private static void put(Map<String, Object> root, List<String> path, String value) {
        Map<String, Object> current = root;
        for (int i = 0; i < path.size() - 1; i++) {
            String key = keyFor(current, path.get(i));
            Object child = current.get(key);
            if (!(child instanceof Map)) {
                child = new LinkedHashMap<String, Object>();
                current.put(key, child);
            }
            current = (Map<String, Object>) child;
        }
        current.put(keyFor(current, path.get(path.size() - 1)), value);
    }

    private static String keyFor(Map<String, Object> map, String segment) {
        return segment.isEmpty() ? String.valueOf(map.size()) : segment;
    }
}
