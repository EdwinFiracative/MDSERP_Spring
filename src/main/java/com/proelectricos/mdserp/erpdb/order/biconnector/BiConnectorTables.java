package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.order.biconnector.BiTable.Column;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiTable.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Tablas que el conector BI publica en Bitrix24 BI Builder: "pedidos_lineas", "pedidos_notas" y
 * "pedidos_margen" (margen real con costo de OP vs estandar, desde vw_MargenPedidoReferencia).
 * Todas tienen la columna fecha_pedido, que es la que acota el rango de consulta.
 */
@Component
public class BiConnectorTables {

    public static final String DATE_COLUMN = "fecha_pedido";

    private final List<BiTable<?>> tables;

    public BiConnectorTables(BiOrderDatasetService datasetService, BiMarginDatasetService marginService) {
        BiTable<BiOrderLineRow> lines = new BiTable<>("pedidos_lineas", "Pedidos - lineas", List.of(
                new Column<>("id_linea", "Id linea", Type.INT, BiOrderLineRow::idLinea),
                new Column<>("clave_linea", "Clave linea", Type.STRING, BiOrderLineRow::claveLinea),
                new Column<>("id_pedido", "Id pedido", Type.INT, BiOrderLineRow::idPedido),
                new Column<>("numero_pedido", "Numero pedido", Type.INT, BiOrderLineRow::numeroPedido),
                new Column<>(DATE_COLUMN, "Fecha pedido", Type.DATE, BiOrderLineRow::fechaPedido),
                new Column<>("condicion_pago", "Condicion de pago", Type.STRING, BiOrderLineRow::condicionPago),
                new Column<>("condicion_pago_norm", "Condicion de pago (normalizada)", Type.STRING, BiOrderLineRow::condicionPagoNorm),
                new Column<>("descripcion_pedido", "Descripcion pedido", Type.STRING, BiOrderLineRow::descripcionPedido),
                new Column<>("sede_id", "Id sede", Type.INT, BiOrderLineRow::sedeId),
                new Column<>("sede_codigo", "Codigo sede", Type.STRING, BiOrderLineRow::sedeCodigo),
                new Column<>("sede_ciudad", "Ciudad sede", Type.STRING, BiOrderLineRow::sedeCiudad),
                new Column<>("sede_direccion", "Direccion sede", Type.STRING, BiOrderLineRow::sedeDireccion),
                new Column<>("sede_zona", "Zona sede", Type.STRING, BiOrderLineRow::sedeZona),
                new Column<>("cliente_id", "Id cliente", Type.INT, BiOrderLineRow::clienteId),
                new Column<>("cliente_nit", "NIT cliente", Type.STRING, BiOrderLineRow::clienteNit),
                new Column<>("cliente_nombre", "Cliente", Type.STRING, BiOrderLineRow::clienteNombre),
                new Column<>("vendedor_id", "Id vendedor", Type.INT, BiOrderLineRow::vendedorId),
                new Column<>("vendedor_codigo", "Codigo vendedor", Type.STRING, BiOrderLineRow::vendedorCodigo),
                new Column<>("vendedor_cedula", "Cedula vendedor", Type.STRING, BiOrderLineRow::vendedorCedula),
                new Column<>("vendedor_nombre", "Vendedor", Type.STRING, BiOrderLineRow::vendedorNombre),
                new Column<>("posicion", "Posicion", Type.INT, BiOrderLineRow::posicion),
                new Column<>("producto_id", "Id producto", Type.INT, BiOrderLineRow::productoId),
                new Column<>("producto_codigo", "Codigo producto", Type.STRING, BiOrderLineRow::productoCodigo),
                new Column<>("producto_nombre", "Producto", Type.STRING, BiOrderLineRow::productoNombre),
                new Column<>("producto_descripcion", "Descripcion producto", Type.STRING, BiOrderLineRow::productoDescripcion),
                new Column<>("unidad_medida", "Unidad de medida", Type.STRING, BiOrderLineRow::unidadMedida),
                new Column<>("linea_negocio_id", "Id linea de negocio", Type.INT, BiOrderLineRow::lineaNegocioId),
                new Column<>("linea_negocio", "Linea de negocio", Type.STRING, BiOrderLineRow::lineaNegocio),
                new Column<>("cantidad", "Cantidad", Type.DOUBLE, BiOrderLineRow::cantidad),
                new Column<>("precio_unitario", "Precio unitario (COP)", Type.DOUBLE, BiOrderLineRow::precioUnitario),
                new Column<>("valor_total", "Valor total sin impuestos (COP)", Type.DOUBLE, BiOrderLineRow::valorTotal),
                new Column<>("fecha_entrega", "Fecha entrega", Type.DATE, BiOrderLineRow::fechaEntrega),
                new Column<>("proyecto_id", "Id proyecto", Type.INT, BiOrderLineRow::proyectoId),
                new Column<>("proyecto", "Proyecto", Type.STRING, BiOrderLineRow::proyecto),
                new Column<>("estado_id", "Id estado", Type.INT, BiOrderLineRow::estadoId),
                new Column<>("estado", "Estado", Type.STRING, BiOrderLineRow::estado)),
                datasetService::findLines);

        BiTable<BiOrderNoteRow> notes = new BiTable<>("pedidos_notas", "Pedidos - notas", List.of(
                new Column<>("numero_pedido", "Numero pedido", Type.INT, BiOrderNoteRow::numeroPedido),
                new Column<>(DATE_COLUMN, "Fecha pedido", Type.DATE, BiOrderNoteRow::fechaPedido),
                new Column<>("id_nota", "Id nota", Type.INT, BiOrderNoteRow::idNota),
                new Column<>("posicion", "Posicion", Type.INT, BiOrderNoteRow::posicion),
                new Column<>("texto", "Texto", Type.STRING, BiOrderNoteRow::texto)),
                datasetService::findNotes);

        // Mismo orden de la vista. Margen % agrupado = SUM(margen_real) / SUM(valor_con_costo_real); igual el estandar
        BiTable<BiMarginRow> margin = new BiTable<>("pedidos_margen", "Pedidos - margen real vs estandar", List.of(
                new Column<>("id_linea", "Id linea", Type.INT, BiMarginRow::idLinea),
                new Column<>("clave_linea", "Clave linea", Type.STRING, BiMarginRow::claveLinea),
                new Column<>("id_pedido", "Id pedido", Type.INT, BiMarginRow::idPedido),
                new Column<>("numero_pedido", "Numero pedido", Type.INT, BiMarginRow::numeroPedido),
                new Column<>(DATE_COLUMN, "Fecha pedido", Type.DATE, BiMarginRow::fechaPedido),
                new Column<>("orden_cliente", "Orden de compra cliente", Type.STRING, BiMarginRow::ordenCliente),
                new Column<>("sede_codigo", "Codigo sede", Type.STRING, BiMarginRow::sedeCodigo),
                new Column<>("cliente_id", "Id cliente", Type.INT, BiMarginRow::clienteId),
                new Column<>("cliente_nit", "NIT cliente", Type.STRING, BiMarginRow::clienteNit),
                new Column<>("cliente_nombre", "Cliente", Type.STRING, BiMarginRow::clienteNombre),
                new Column<>("vendedor_codigo", "Codigo vendedor", Type.STRING, BiMarginRow::vendedorCodigo),
                new Column<>("vendedor_nombre", "Vendedor", Type.STRING, BiMarginRow::vendedorNombre),
                new Column<>("estado", "Estado", Type.STRING, BiMarginRow::estado),
                new Column<>("producto_id", "Id producto", Type.INT, BiMarginRow::productoId),
                new Column<>("linea_negocio_id", "Id linea de negocio", Type.INT, BiMarginRow::lineaNegocioId),
                new Column<>("posicion", "Posicion", Type.INT, BiMarginRow::posicion),
                new Column<>("producto_codigo", "Codigo producto", Type.STRING, BiMarginRow::productoCodigo),
                new Column<>("producto_nombre", "Producto", Type.STRING, BiMarginRow::productoNombre),
                new Column<>("linea_negocio", "Linea de negocio", Type.STRING, BiMarginRow::lineaNegocio),
                new Column<>("cantidad", "Cantidad", Type.DOUBLE, BiMarginRow::cantidad),
                new Column<>("precio_unitario", "Precio unitario (COP)", Type.DOUBLE, BiMarginRow::precioUnitario),
                new Column<>("valor_pedido", "Valor pedido sin impuestos (COP)", Type.DOUBLE, BiMarginRow::valorPedido),
                new Column<>("costo_real_unitario", "Costo real unitario (COP)", Type.DOUBLE, BiMarginRow::costoRealUnitario),
                new Column<>("costo_estandar_unitario", "Costo estandar unitario (COP)", Type.DOUBLE, BiMarginRow::costoEstandarUnitario),
                new Column<>("diferencia_costo_pct", "Diferencia costo real vs estandar % de la linea", Type.DOUBLE, BiMarginRow::diferenciaCostoPct),
                new Column<>("margen_real_pct", "Margen real % de la linea", Type.DOUBLE, BiMarginRow::margenRealPct),
                new Column<>("margen_estandar_pct", "Margen estandar % de la linea", Type.DOUBLE, BiMarginRow::margenEstandarPct),
                new Column<>("costo_real", "Costo real (COP)", Type.DOUBLE, BiMarginRow::costoReal),
                new Column<>("costo_estandar", "Costo estandar (COP)", Type.DOUBLE, BiMarginRow::costoEstandar),
                new Column<>("diferencia_costo", "Diferencia costo real - estandar (COP)", Type.DOUBLE, BiMarginRow::diferenciaCosto),
                new Column<>("margen_real", "Margen real (COP)", Type.DOUBLE, BiMarginRow::margenReal),
                new Column<>("margen_estandar", "Margen estandar (COP)", Type.DOUBLE, BiMarginRow::margenEstandar),
                new Column<>("fuente_costo_real", "Fuente del costo real", Type.STRING, BiMarginRow::fuenteCostoReal),
                new Column<>("num_op", "Numero de OP usadas", Type.INT, BiMarginRow::numOp),
                new Column<>("ultima_op", "Ultima OP", Type.INT, BiMarginRow::ultimaOp),
                new Column<>("mp_unitario", "Materia prima unitaria (COP)", Type.DOUBLE, BiMarginRow::mpUnitario),
                new Column<>("mo_unitario", "Mano de obra unitaria (COP)", Type.DOUBLE, BiMarginRow::moUnitario),
                new Column<>("cif_unitario", "CIF unitario (COP)", Type.DOUBLE, BiMarginRow::cifUnitario),
                new Column<>("st_unitario", "Servicios de terceros unitario (COP)", Type.DOUBLE, BiMarginRow::stUnitario),
                new Column<>("diferencia_costo_unitario", "Diferencia costo unitario real - estandar (COP)", Type.DOUBLE, BiMarginRow::diferenciaCostoUnitario),
                new Column<>("valor_con_costo_real", "Valor pedido con costo real (COP)", Type.DOUBLE, BiMarginRow::valorConCostoReal),
                new Column<>("valor_con_costo_estandar", "Valor pedido con costo estandar (COP)", Type.DOUBLE, BiMarginRow::valorConCostoEstandar)),
                marginService::findLines);

        this.tables = List.of(lines, notes, margin);
    }

    public List<BiTable<?>> all() {
        return tables;
    }

    public Optional<BiTable<?>> find(String code) {
        return tables.stream().filter(table -> table.code().equals(code)).findFirst();
    }
}
