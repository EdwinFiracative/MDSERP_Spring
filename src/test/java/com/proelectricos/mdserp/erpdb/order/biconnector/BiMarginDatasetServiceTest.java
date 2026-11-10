package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.biconnector.biconnector.BiMarginDatasetService;
import com.proelectricos.mdserp.erpdb.biconnector.biconnector.BiMarginRow;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BiMarginDatasetServiceTest {

    private static ResultSet resultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("orderReferId", Long.class)).thenReturn(901L);
        when(rs.getObject("orderHeaderId", Long.class)).thenReturn(55L);
        when(rs.getObject("Pedido", Integer.class)).thenReturn(49594);
        when(rs.getObject("Item", Integer.class)).thenReturn(3);
        when(rs.getDate("Fecha")).thenReturn(Date.valueOf("2026-10-15"));
        when(rs.getString("OrdenCliente")).thenReturn("OC-12 ");
        when(rs.getString("Sede")).thenReturn("01");
        when(rs.getObject("clientId", Long.class)).thenReturn(7L);
        when(rs.getObject("NitCliente", Long.class)).thenReturn(900123456L);
        when(rs.getString("Cliente")).thenReturn("CLIENTE SAS");
        when(rs.getString("CodigoVendedor")).thenReturn("V01");
        when(rs.getString("Vendedor")).thenReturn("VENDEDOR UNO");
        when(rs.getObject("referId", Long.class)).thenReturn(321L);
        when(rs.getString("CodigoReferencia")).thenReturn("41154937104");
        when(rs.getString("Referencia")).thenReturn("TABLERO\r\n  ESPECIAL");
        when(rs.getString("EstadoLinea")).thenReturn("Pendiente");
        when(rs.getObject("LineaNegocioId", Long.class)).thenReturn(1152L);
        when(rs.getString("LineaNegocio")).thenReturn("conduit");
        when(rs.getBigDecimal("Cantidad")).thenReturn(new BigDecimal("2.0000"));
        when(rs.getBigDecimal("PrecioUnitario")).thenReturn(new BigDecimal("1000.0000"));
        when(rs.getBigDecimal("ValorPedido")).thenReturn(new BigDecimal("2000.00"));
        when(rs.getBigDecimal("CostoRealUnitario")).thenReturn(new BigDecimal("600.0000"));
        when(rs.getBigDecimal("CostoEstandarUnitario")).thenReturn(new BigDecimal("500.0000"));
        when(rs.getBigDecimal("MargenRealPct")).thenReturn(new BigDecimal("40.00"));
        when(rs.getBigDecimal("MargenEstandarPct")).thenReturn(new BigDecimal("50.00"));
        when(rs.getBigDecimal("DiferenciaCosto")).thenReturn(new BigDecimal("200.00"));
        when(rs.getBigDecimal("ValorConCostoEstandar")).thenReturn(new BigDecimal("2000.00"));
        when(rs.getString("FuenteCostoReal")).thenReturn("OP periodo");
        when(rs.getObject("NumOP", Integer.class)).thenReturn(2);
        when(rs.getObject("UltimaOP", Integer.class)).thenReturn(44937104);
        return rs;
    }

    @Test
    void mapeaLaFilaDeLaVista() throws Exception {
        BiMarginRow row = BiMarginDatasetService.toRow(resultSet());

        assertThat(row.claveLinea()).isEqualTo("49594-3");
        assertThat(row.fechaPedido()).isEqualTo(LocalDate.of(2026, 10, 15));
        assertThat(row.ordenCliente()).isEqualTo("OC-12");
        assertThat(row.clienteNit()).isEqualTo("900123456");
        assertThat(row.productoNombre()).isEqualTo("TABLERO ESPECIAL");
        assertThat(row.lineaNegocioId()).isEqualTo(1152L);
        assertThat(row.lineaNegocio()).isEqualTo("conduit");
        assertThat(row.valorPedido()).isEqualByComparingTo("2000");
        assertThat(row.costoRealUnitario()).isEqualByComparingTo("600");
        assertThat(row.costoEstandarUnitario()).isEqualByComparingTo("500");
        assertThat(row.margenRealPct()).isEqualByComparingTo("40");
        assertThat(row.margenEstandarPct()).isEqualByComparingTo("50");
        assertThat(row.diferenciaCosto()).isEqualByComparingTo("200");
        assertThat(row.valorConCostoEstandar()).isEqualByComparingTo("2000");
        assertThat(row.fuenteCostoReal()).isEqualTo("OP periodo");
        assertThat(row.ultimaOp()).isEqualTo(44937104);
        // Sin desglose en la vista (p.ej. costo 'Estandar'): queda en null, no en cero
        assertThat(row.mpUnitario()).isNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    void consultaLaVistaPorRangoDeFechaInclusivo() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.query(eq(BiMarginDatasetService.SQL), any(RowMapper.class), any(), any())).thenReturn(List.of());

        new BiMarginDatasetService(jdbc).findLines(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));

        verify(jdbc).query(eq(BiMarginDatasetService.SQL), any(RowMapper.class),
                eq(Date.valueOf("2026-10-01")), eq(Date.valueOf("2026-10-31")));
        assertThat(BiMarginDatasetService.SQL).contains("vw_MargenPedidoReferencia").contains("Fecha <= ?");
    }
}
