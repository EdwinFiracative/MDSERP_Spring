package com.proelectricos.mdserp.chatbot.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Convierte el Markdown que genera el modelo al HTML que acepta Telegram (parse_mode=HTML).
 * Telegram no soporta tablas, así que se renderizan alineadas dentro de &lt;pre&gt;.
 */
final class TelegramFormatter {

    private static final Pattern NEGRITA = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern CODIGO = Pattern.compile("`([^`]+)`");
    private static final Pattern TITULO = Pattern.compile("^#{1,6}\\s+(.*)$");
    private static final Pattern VINETA = Pattern.compile("^(\\s*)[-*]\\s+");
    private static final Pattern SEPARADOR_TABLA = Pattern.compile("^\\s*\\|?[\\s:|-]+\\|?\\s*$");

    private TelegramFormatter() {
    }

    static String html(String markdown) {
        StringBuilder html = new StringBuilder();
        List<String> tabla = new ArrayList<>();
        boolean enBloqueCodigo = false;
        for (String linea : markdown.split("\n", -1)) {
            if (linea.trim().startsWith("```")) {
                html.append(enBloqueCodigo ? "</pre>\n" : "<pre>");
                enBloqueCodigo = !enBloqueCodigo;
                continue;
            }
            if (enBloqueCodigo) {
                html.append(escapar(linea)).append('\n');
                continue;
            }
            if (linea.trim().startsWith("|")) {
                tabla.add(linea);
                continue;
            }
            if (!tabla.isEmpty()) {
                html.append(tablaAlineada(tabla));
                tabla.clear();
            }
            html.append(lineaHtml(linea)).append('\n');
        }
        if (!tabla.isEmpty()) {
            html.append(tablaAlineada(tabla));
        }
        if (enBloqueCodigo) {
            html.append("</pre>");
        }
        return html.toString().trim();
    }

    /** Parte el texto en trozos que quepan en un mensaje de Telegram (4096 caracteres), cortando en saltos de línea. */
    static List<String> partir(String texto, int maximo) {
        List<String> partes = new ArrayList<>();
        String resto = texto;
        while (resto.length() > maximo) {
            int corte = resto.lastIndexOf('\n', maximo);
            if (corte <= 0) {
                corte = maximo;
            }
            partes.add(resto.substring(0, corte));
            resto = resto.substring(corte).stripLeading();
        }
        partes.add(resto);
        return partes;
    }

    private static String lineaHtml(String linea) {
        String html = escapar(linea);
        var titulo = TITULO.matcher(html);
        if (titulo.matches()) {
            html = "<b>" + titulo.group(1).replace("**", "") + "</b>";
        }
        html = VINETA.matcher(html).replaceFirst("$1• ");
        html = NEGRITA.matcher(html).replaceAll("<b>$1</b>");
        return CODIGO.matcher(html).replaceAll("<code>$1</code>");
    }

    private static String tablaAlineada(List<String> lineas) {
        List<String[]> filas = new ArrayList<>();
        int columnas = 0;
        for (String linea : lineas) {
            if (SEPARADOR_TABLA.matcher(linea).matches()) {
                continue;
            }
            String contenido = linea.trim().replaceAll("^\\|", "").replaceAll("\\|$", "");
            String[] celdas = contenido.split("\\|", -1);
            for (int i = 0; i < celdas.length; i++) {
                celdas[i] = celdas[i].replace("**", "").replace("`", "").trim();
            }
            filas.add(celdas);
            columnas = Math.max(columnas, celdas.length);
        }
        int[] anchos = new int[columnas];
        for (String[] fila : filas) {
            for (int i = 0; i < fila.length; i++) {
                anchos[i] = Math.max(anchos[i], fila[i].length());
            }
        }
        StringBuilder pre = new StringBuilder("<pre>");
        for (int f = 0; f < filas.size(); f++) {
            String[] fila = filas.get(f);
            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < columnas; i++) {
                String celda = i < fila.length ? fila[i] : "";
                texto.append(i == 0 ? "" : " | ").append(String.format("%-" + Math.max(anchos[i], 1) + "s", celda));
            }
            pre.append(escapar(texto.toString().stripTrailing())).append('\n');
            if (f == 0 && filas.size() > 1) {
                StringBuilder raya = new StringBuilder();
                for (int i = 0; i < columnas; i++) {
                    raya.append(i == 0 ? "" : "-+-").append("-".repeat(Math.max(anchos[i], 1)));
                }
                pre.append(raya).append('\n');
            }
        }
        return pre.append("</pre>\n").toString();
    }

    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
