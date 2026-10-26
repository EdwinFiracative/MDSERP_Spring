package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.order.biconnector.BiTable.Column;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiTable.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Tablas que el conector BI publica en Bitrix24 BI Builder: "pedidos_lineas" y "pedidos_notas".
 * Todas tienen la columna fecha_pedido, que es la que acota el rango de consulta.
 */
@Component
public class BiConnectorTables {

    public static final String DATE_COLUMN = "fecha_pedido";

    private final List<BiTable<?>> tables;

    public BiConnectorTables(BiOrderDatasetService datasetService) {
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
                new Column<>("vendedor_nombre", "Vendedor", Type.STRING, BiOrderLineRow::vendedorNombre),
                new Column<>("posicion", "Posicion", Type.INT, BiOrderLineRow::posicion),
                new Column<>("producto_id", "Id producto", Type.INT, BiOrderLineRow::productoId),
                new Column<>("producto_codigo", "Codigo producto", Type.STRING, BiOrderLineRow::productoCodigo),
                new Column<>("producto_nombre", "Producto", Type.STRING, BiOrderLineRow::productoNombre),
                new Column<>("producto_descripcion", "Descripcion producto", Type.STRING, BiOrderLineRow::productoDescripcion),
                new Column<>("unidad_medida", "Unidad de medida", Type.STRING, BiOrderLineRow::unidadMedida),
                new Column<>("linea_negocio_id", "Id linea de negocio", Type.INT, BiOrderLineRow::lineaNegocioId),
                new Column<>("linea_negocio", "Linea de negocio", Type.STRING, BiOrderLineRow::lineaNegocio),
                new Column<>("cantidad", "Cantidad", Type.INT, BiOrderLineRow::cantidad),
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

        this.tables = List.of(lines, notes);
    }

    public List<BiTable<?>> all() {
        return tables;
    }

    public Optional<BiTable<?>> find(String code) {
        return tables.stream().filter(table -> table.code().equals(code)).findFirst();
    }
}
