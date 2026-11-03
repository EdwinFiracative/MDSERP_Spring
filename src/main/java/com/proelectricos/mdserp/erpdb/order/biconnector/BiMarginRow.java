package com.proelectricos.mdserp.erpdb.order.biconnector;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Fila de la tabla "pedidos_margen" del conector BI de Bitrix24: una por linea de pedido (sin anuladas ni
 * canceladas) con precio, costo de fabricacion tomado de las OP y margen. Sale de la vista
 * ErpDb.dbo.vw_MargenPedidoReferencia via {@link BiMarginDatasetService}.
 */
public record BiMarginRow(
        Long idLinea,
        String claveLinea,
        Long idPedido,
        Integer numeroPedido,
        LocalDate fechaPedido,
        String ordenCliente,
        String sedeCodigo,
        Long clienteId,
        String clienteNit,
        String clienteNombre,
        String vendedorCodigo,
        String vendedorNombre,
        Integer posicion,
        Long productoId,
        String productoCodigo,
        String productoNombre,
        String estado,
        BigDecimal cantidad,
        BigDecimal precioUnitario,
        BigDecimal costoUnitario,
        BigDecimal mpUnitario,
        BigDecimal moUnitario,
        BigDecimal cifUnitario,
        BigDecimal stUnitario,
        BigDecimal venta,
        BigDecimal ventaConCosto,
        BigDecimal costo,
        BigDecimal margen,
        BigDecimal margenPct,
        String fuenteCosto,
        Integer numOp,
        Integer ultimaOp
) {
}
