package com.proelectricos.mdserp.erpdb.biconnector.biconnector;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Fila de la tabla "pedidos_margen" del conector BI de Bitrix24: una por linea de pedido (sin anuladas ni
 * canceladas) con el valor del pedido y dos margenes lado a lado: REAL (costo de las OP de EMP001_INV) y
 * ESTANDAR (MAESTRO.CSTD). Sale de la vista ErpDb.dbo.vw_MargenPedidoReferencia via
 * {@link BiMarginDatasetService}; los campos siguen el orden de la vista.
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
        String estado,
        Long productoId,
        Long lineaNegocioId,
        Integer posicion,
        String productoCodigo,
        String productoNombre,
        String lineaNegocio,
        BigDecimal cantidad,
        BigDecimal precioUnitario,
        BigDecimal valorPedido,
        BigDecimal costoRealUnitario,
        BigDecimal costoEstandarUnitario,
        BigDecimal diferenciaCostoPct,
        BigDecimal margenRealPct,
        BigDecimal margenEstandarPct,
        BigDecimal costoReal,
        BigDecimal costoEstandar,
        BigDecimal diferenciaCosto,
        BigDecimal margenReal,
        BigDecimal margenEstandar,
        String fuenteCostoReal,
        Integer numOp,
        Integer ultimaOp,
        BigDecimal mpUnitario,
        BigDecimal moUnitario,
        BigDecimal cifUnitario,
        BigDecimal stUnitario,
        BigDecimal diferenciaCostoUnitario,
        BigDecimal valorConCostoReal,
        BigDecimal valorConCostoEstandar
) {
}
