package com.proelectricos.mdserp.erpdb.order.biconnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Lee la vista ErpDb.dbo.vw_MargenPedidoReferencia (margen por linea de pedido con costo de las OP de
 * EMP001_INV) para la tabla "pedidos_margen" del conector BI. Cada linea consulta OP, por eso siempre se
 * filtra por fecha: el controlador pide de a un mes (~1.500 lineas, pocos segundos).
 */
@Service
public class BiMarginDatasetService {

    static final String SQL = """
            SELECT orderReferId, orderHeaderId, Pedido, Fecha, OrdenCliente, Sede, clientId, NitCliente, Cliente,
                   CodigoVendedor, Vendedor, Item, referId, CodigoReferencia, Referencia, EstadoLinea,
                   Cantidad, PrecioUnitario, CostoUnitario, MPUnit, MOUnit, CIFUnit, STUnit,
                   Venta, VentaConCosto, Costo, Margen, MargenPct, FuenteCosto, NumOP, UltimaOP
            FROM ErpDb.dbo.vw_MargenPedidoReferencia
            WHERE Fecha >= ? AND Fecha <= ?
            ORDER BY Pedido, Item""";

    // Un mes completo responde en pocos segundos; el margen cubre meses con mas OP
    private static final int QUERY_TIMEOUT_SECONDS = 120;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BiMarginDatasetService(@Qualifier("sqlFactoryDataSource") DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    BiMarginDatasetService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
    }

    // Lineas de los pedidos con fecha entre from y to (ambas incluidas), ordenadas por pedido e item
    public List<BiMarginRow> findLines(LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SQL, (rs, rowNum) -> toRow(rs), Date.valueOf(from), Date.valueOf(to));
    }

    static BiMarginRow toRow(ResultSet rs) throws SQLException {
        Integer pedido = rs.getObject("Pedido", Integer.class);
        Integer item = rs.getObject("Item", Integer.class);
        Long nit = rs.getObject("NitCliente", Long.class);
        Date fecha = rs.getDate("Fecha");
        return new BiMarginRow(
                rs.getObject("orderReferId", Long.class),
                pedido + "-" + item,
                rs.getObject("orderHeaderId", Long.class),
                pedido,
                fecha == null ? null : fecha.toLocalDate(),
                trim(rs.getString("OrdenCliente")),
                trim(rs.getString("Sede")),
                rs.getObject("clientId", Long.class),
                // NIT como texto: en BI es un identificador, no una cantidad
                nit == null ? null : nit.toString(),
                trim(rs.getString("Cliente")),
                trim(rs.getString("CodigoVendedor")),
                trim(rs.getString("Vendedor")),
                item,
                rs.getObject("referId", Long.class),
                trim(rs.getString("CodigoReferencia")),
                BiOrderDatasetService.singleLine(trim(rs.getString("Referencia"))),
                rs.getString("EstadoLinea"),
                rs.getBigDecimal("Cantidad"),
                rs.getBigDecimal("PrecioUnitario"),
                rs.getBigDecimal("CostoUnitario"),
                rs.getBigDecimal("MPUnit"),
                rs.getBigDecimal("MOUnit"),
                rs.getBigDecimal("CIFUnit"),
                rs.getBigDecimal("STUnit"),
                rs.getBigDecimal("Venta"),
                rs.getBigDecimal("VentaConCosto"),
                rs.getBigDecimal("Costo"),
                rs.getBigDecimal("Margen"),
                rs.getBigDecimal("MargenPct"),
                rs.getString("FuenteCosto"),
                rs.getObject("NumOP", Integer.class),
                rs.getObject("UltimaOP", Integer.class));
    }

    private static String trim(String value) {
        return value == null ? null : value.strip();
    }
}
