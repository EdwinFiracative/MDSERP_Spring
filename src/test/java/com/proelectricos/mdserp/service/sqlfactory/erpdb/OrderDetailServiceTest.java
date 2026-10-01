package com.proelectricos.mdserp.service.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailNoteDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailReferenceDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Compara el DTO del detalle de pedido contra la consulta SQL de referencia (solo lectura).
 */
@SpringBootTest
@Transactional(readOnly = true)
class OrderDetailServiceTest {

    private static final int ORDER_NUMBER = 49594;

    private static final String HEADER_SQL = """
            SELECT h.orderHeaderNumber, h.orderHeaderDate, h.orderHeaderClientOrder, RTRIM(b.branchCode),
                   tc.thirdPartyName, b.branchAddress, b.branchCity,
                   CAST(tc.thirdPartyIdentNumber AS nvarchar(20))
                     + ISNULL(N'-' + CAST(tc.thirdPartyVerifDigit AS nvarchar(1)), N''),
                   c.clientCrediCondition, h.orderHeaderPaymeConditions, v.vendorCode, tv.thirdPartyName,
                   h.orderHeaderDescription
            FROM ErpDb.dbo.OrderHeader AS h
            INNER JOIN ErpDb.dbo.Branch     AS b  ON b.branchId      = h.orderHeaderBranch
            INNER JOIN ErpDb.dbo.Client     AS c  ON c.clientId      = b.branchClient
            INNER JOIN ErpDb.dbo.ThirdParty AS tc ON tc.thirdPartyId = c.clientThirdParty
            LEFT  JOIN ErpDb.dbo.Vendor     AS v  ON v.vendorId      = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT  JOIN ErpDb.dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            WHERE h.orderHeaderNumber = ?1
            """;

    private static final String REFERENCES_SQL = """
            SELECT r.orderReferPosition, ref.referCod, ref.referName, mu.measuUnitCode, r.orderReferQuantity,
                   r.orderReferUnitPrice, r.orderReferQuantity * r.orderReferUnitPrice
            FROM ErpDb.dbo.OrderReference AS r
            INNER JOIN ErpDb.dbo.OrderHeader AS h  ON h.orderHeaderId = r.orderReferOrderHeader
            INNER JOIN ErpDb.dbo.Reference  AS ref ON ref.referId    = r.orderReferReference
            INNER JOIN ErpDb.dbo.MeasurUnit AS mu  ON mu.measuUnitId = ref.referMeasuUnit
            WHERE h.orderHeaderNumber = ?1
            ORDER BY r.orderReferPosition
            """;

    private static final String NOTES_SQL = """
            SELECT n.orderNotePosition, n.orderNoteText
            FROM ErpDb.dbo.OrderNote AS n
            INNER JOIN ErpDb.dbo.OrderHeader AS h ON h.orderHeaderId = n.orderNoteOrderHeader
            WHERE h.orderHeaderNumber = ?1
            ORDER BY n.orderNotePosition
            """;

    @Autowired
    private OrderDetailService orderDetailService;

    @PersistenceContext
    private EntityManager em;

    @Test
    void headerMatchesSql() {
        assertHeaderMatchesSql(ORDER_NUMBER);
    }

    @Test
    void orderWithoutVendorTakesBranchVendor() {
        List<?> numbers = em.createNativeQuery("""
                SELECT TOP 1 h.orderHeaderNumber FROM ErpDb.dbo.OrderHeader h
                JOIN ErpDb.dbo.Branch b ON b.branchId = h.orderHeaderBranch
                WHERE h.orderHeaderVendor IS NULL AND b.branchVendor IS NOT NULL
                """).getResultList();
        assertThat(numbers).as("pedido sin vendedor con sede con vendedor").isNotEmpty();

        OrderDetailDto dto = assertHeaderMatchesSql(((Number) numbers.get(0)).intValue());
        assertThat(dto.getVendedor()).isNotNull();
    }

    @Test
    void referencesMatchSql() {
        OrderDetailDto dto = orderDetailService.findByOrderNumber(ORDER_NUMBER).orElseThrow();
        List<Object[]> rows = rows(REFERENCES_SQL, ORDER_NUMBER);

        assertThat(dto.getReferencias()).hasSameSizeAs(rows).isNotEmpty();
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            OrderDetailReferenceDto line = dto.getReferencias().get(i);
            assertThat(line.getItem()).isEqualTo(((Number) row[0]).intValue());
            assertThat(line.getReferencia().getReferCod()).isEqualTo(row[1]);
            assertThat(line.getReferencia().getReferName()).isEqualTo(row[2]);
            assertThat(line.getReferencia().getReferMeasuUnit().getMeasuUnitCode()).isEqualTo(row[3]);
            assertThat(line.getCantidad()).isEqualTo(((Number) row[4]).intValue());
            assertThat(line.getValorUnitario()).isEqualByComparingTo((BigDecimal) row[5]);
            assertThat(line.getValorTotal()).isEqualByComparingTo((BigDecimal) row[6]);
        }
    }

    @Test
    void notesMatchSql() {
        OrderDetailDto dto = orderDetailService.findByOrderNumber(ORDER_NUMBER).orElseThrow();
        List<Object[]> rows = rows(NOTES_SQL, ORDER_NUMBER);

        assertThat(dto.getNotas()).extracting(OrderDetailNoteDto::getPosicion)
                .containsExactlyElementsOf(rows.stream().map(r -> ((Number) r[0]).intValue()).toList());
        assertThat(dto.getNotas()).extracting(OrderDetailNoteDto::getNota)
                .containsExactlyElementsOf(rows.stream().map(r -> (String) r[1]).toList());
    }

    @Test
    void lastDaysReturnsOnlyOrdersInRangeSorted() {
        LocalDate from = LocalDate.now().minusDays(90);
        long expected = ((Number) em.createNativeQuery(
                        "SELECT COUNT(*) FROM ErpDb.dbo.OrderHeader WHERE orderHeaderDate >= ?1")
                .setParameter(1, from).getSingleResult()).longValue();

        List<OrderDetailDto> page = orderDetailService.findLastDays(90,
                PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "orderHeaderNumber")));

        assertThat(page).hasSize((int) Math.min(expected, 50));
        assertThat(page).allSatisfy(dto -> assertThat(dto.getFecha()).isAfterOrEqualTo(from));
        assertThat(page).extracting(OrderDetailDto::getPedido).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void lastDaysDetailMatchesSingleOrderDetail() {
        List<OrderDetailDto> page = orderDetailService.findLastDays(90,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "orderHeaderNumber")));
        assertThat(page).as("pedidos en los ultimos 90 dias").isNotEmpty();

        for (OrderDetailDto fromList : page) {
            OrderDetailDto single = orderDetailService.findByOrderNumber(fromList.getPedido()).orElseThrow();
            assertThat(fromList).usingRecursiveComparison().isEqualTo(single);
        }
    }

    @Test
    void unknownOrderReturnsEmpty() {
        assertThat(orderDetailService.findByOrderNumber(-1)).isEmpty();
    }

    private OrderDetailDto assertHeaderMatchesSql(int orderNumber) {
        OrderDetailDto dto = orderDetailService.findByOrderNumber(orderNumber).orElseThrow();
        Object[] row = rows(HEADER_SQL, orderNumber).get(0);

        assertThat(dto.getPedido()).isEqualTo(((Number) row[0]).intValue());
        assertThat(dto.getFecha()).isEqualTo(((Date) row[1]).toLocalDate());
        assertThat(dto.getOrdenCliente()).isEqualTo(row[2]);
        assertThat(dto.getSede().getBranchCode()).isEqualTo(row[3]);
        assertThat(dto.getCliente().getClientThirdParty().getThirdPartyName()).isEqualTo(row[4]);
        assertThat(dto.getSede().getBranchAddress()).isEqualTo(row[5]);
        assertThat(dto.getSede().getBranchCity()).isEqualTo(row[6]);
        assertThat(dto.getCliente().getClientThirdParty().getNit()).isEqualTo(row[7]);
        assertThat(dto.getCliente().getClientCrediCondition()).isEqualTo(row[8]);
        assertThat(dto.getCondicionPagoPedido()).isEqualTo(row[9]);
        assertThat(dto.getVendedor() == null ? null : dto.getVendedor().getVendorCode()).isEqualTo(row[10]);
        assertThat(dto.getVendedor() == null ? null : dto.getVendedor().getVendorThirdParty().getThirdPartyName()).isEqualTo(row[11]);
        assertThat(dto.getDescripcion()).isEqualTo(row[12]);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(String sql, int orderNumber) {
        return em.createNativeQuery(sql).setParameter(1, orderNumber).getResultList();
    }
}
