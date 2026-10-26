package com.proelectricos.mdserp.erpdb.order.biconnector;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Fila de la tabla "pedidos_lineas" del conector BI de Bitrix24: una por linea de pedido,
 * con los datos de cabecera repetidos. Construida desde
 * {@link com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto} por {@link BiOrderDatasetService}.
 */
public record BiOrderLineRow(
        Long idLinea,
        String claveLinea,
        Long idPedido,
        Integer numeroPedido,
        LocalDate fechaPedido,
        String condicionPago,
        String condicionPagoNorm,
        String descripcionPedido,
        Long sedeId,
        String sedeCodigo,
        String sedeCiudad,
        String sedeDireccion,
        Long clienteId,
        String clienteNit,
        String clienteNombre,
        Long vendedorId,
        String vendedorCodigo,
        String vendedorNombre,
        Integer posicion,
        Long productoId,
        String productoCodigo,
        String productoNombre,
        String productoDescripcion,
        String unidadMedida,
        Long lineaNegocioId,
        String lineaNegocio,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal valorTotal,
        LocalDate fechaEntrega,
        Long proyectoId,
        String proyecto,
        Long estadoId,
        String estado
) {
}
