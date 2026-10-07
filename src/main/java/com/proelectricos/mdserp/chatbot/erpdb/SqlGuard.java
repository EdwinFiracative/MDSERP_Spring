package com.proelectricos.mdserp.chatbot.erpdb;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Valida el SQL que genera el modelo antes de ejecutarlo: una sola consulta de lectura
 * (SELECT / WITH) que no salga de ErpDb.
 * Es una segunda barrera; la principal debe ser un usuario SQL con solo lectura sobre ErpDb.
 */
public final class SqlGuard {

    private static final String BASE_PERMITIDA = "erpdb";

    private static final Pattern COMENTARIOS = Pattern.compile("--[^\\n]*|/\\*.*?\\*/", Pattern.DOTALL);
    private static final Pattern LITERALES = Pattern.compile("'(?:[^']|'')*'");
    private static final Pattern PROHIBIDAS = Pattern.compile(
            "\\b(insert|update|delete|merge|drop|alter|create|truncate|exec|execute|grant|revoke|deny"
                    + "|backup|restore|dbcc|shutdown|kill|use|into|declare|set|waitfor|reconfigure|bulk"
                    + "|openrowset|opendatasource|openquery|openxml)\\b"
                    + "|\\b(xp|sp)_\\w*|\\bsys\\s*\\.",
            Pattern.CASE_INSENSITIVE);
    // Nombres de tres partes: base.esquema.objeto (o base..objeto)
    private static final Pattern TRES_PARTES = Pattern.compile(
            "(\\[[^\\]]+\\]|\\w+)\\s*\\.\\s*(\\[[^\\]]*\\]|\\w*)\\s*\\.\\s*(\\[[^\\]]+\\]|\\w+)");

    private SqlGuard() {
    }

    /**
     * @return la consulta sin comentarios ni punto y coma final, lista para ejecutar
     * @throws IllegalArgumentException con un mensaje que el modelo puede usar para corregirla
     */
    public static String validar(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("La consulta está vacía.");
        }
        String limpia = COMENTARIOS.matcher(sql).replaceAll(" ").trim();
        while (limpia.endsWith(";")) {
            limpia = limpia.substring(0, limpia.length() - 1).trim();
        }
        // Las palabras dentro de textos ('...') no cuentan para la validación
        String sinLiterales = LITERALES.matcher(limpia).replaceAll("''");

        if (sinLiterales.contains(";")) {
            throw new IllegalArgumentException("Solo se permite una consulta por llamada.");
        }
        String inicio = sinLiterales.toLowerCase(Locale.ROOT);
        if (!inicio.startsWith("select") && !inicio.startsWith("with")) {
            throw new IllegalArgumentException("Solo se permiten consultas SELECT (o WITH ... SELECT).");
        }
        Matcher prohibida = PROHIBIDAS.matcher(sinLiterales);
        if (prohibida.find()) {
            throw new IllegalArgumentException("Instrucción no permitida en el chatbot: " + prohibida.group().trim()
                    + ". Solo hay acceso de lectura a ErpDb.");
        }
        Matcher tresPartes = TRES_PARTES.matcher(sinLiterales);
        while (tresPartes.find()) {
            String base = tresPartes.group(1).replace("[", "").replace("]", "").trim();
            if (!base.equalsIgnoreCase(BASE_PERMITIDA)) {
                throw new IllegalArgumentException("Solo se puede consultar la base ErpDb (se encontró: "
                        + tresPartes.group(1) + ").");
            }
        }
        return limpia;
    }
}
