package com.proelectricos.mdserp.erpdb.order.biconnector;

import java.time.LocalDate;

/**
 * Fila de la tabla "pedidos_notas" del conector BI de Bitrix24: una por nota de pedido.
 * Lleva la fecha del pedido para poder filtrar por rango igual que las lineas.
 */
public record BiOrderNoteRow(
        Integer numeroPedido,
        LocalDate fechaPedido,
        Long idNota,
        Integer posicion,
        String texto
) {
}
