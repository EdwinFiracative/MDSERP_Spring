package com.proelectricos.mdserp.erpdb.order.service;

import com.proelectricos.mdserp.erpdb.branch.dto.BranchDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailNoteDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailReferenceDto;
import com.proelectricos.mdserp.erpdb.reference.dto.ReferenceDto;
import com.proelectricos.mdserp.erpdb.thirdparty.dto.ThirdPartyDto;
import com.proelectricos.mdserp.erpdb.vendor.dto.VendorDto;
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
            SELECT h.orderHeaderId, h.orderHeaderNumber, h.orderHeaderDate,
                   h.orderHeaderPaymeConditions, h.orderHeaderDescription,
                   b.branchId, RTRIM(b.branchCode), b.branchCity, b.branchAddress,
                   c.clientId, tc.thirdPartyId, tc.thirdPartyIdentNumber, tc.thirdPartyName,
                   v.vendorId, v.vendorCode, tv.thirdPartyId, tv.thirdPartyName
            FROM ErpDb.dbo.OrderHeader AS h
            INNER JOIN ErpDb.dbo.Branch     AS b  ON b.branchId      = h.orderHeaderBranch
            INNER JOIN ErpDb.dbo.Client     AS c  ON c.clientId      = b.branchClient
            INNER JOIN ErpDb.dbo.ThirdParty AS tc ON tc.thirdPartyId = c.clientThirdParty
            LEFT  JOIN ErpDb.dbo.Vendor     AS v  ON v.vendorId      = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT  JOIN ErpDb.dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            WHERE h.orderHeaderNumber = ?1
            """;

    private static final String REFERENCES_SQL = """
            SELECT r.orderReferId, r.orderReferPosition, ref.referId, ref.referCod, ref.referCod2, ref.referName,
                   ref.referDescription, mu.measuUnitId, mu.measuUnitName, r.orderReferQuantity,
                   r.orderReferUnitPrice, r.orderReferQuantity * r.orderReferUnitPrice,
                   r.orderReferDelivDate, s.orderReferStatusId, s.orderReferStatusName,
                   p.projeId, p.projeName
            FROM ErpDb.dbo.OrderReference AS r
            INNER JOIN ErpDb.dbo.OrderHeader AS h  ON h.orderHeaderId = r.orderReferOrderHeader
            INNER JOIN ErpDb.dbo.Reference  AS ref ON ref.referId    = r.orderReferReference
            INNER JOIN ErpDb.dbo.MeasurUnit AS mu  ON mu.measuUnitId = ref.referMeasuUnit
            INNER JOIN ErpDb.dbo.OrderReferStatus AS s ON s.orderReferStatusId = r.orderReferStatus
            LEFT  JOIN ErpDb.dbo.Project    AS p   ON p.projeId      = r.orderReferProject
            WHERE h.orderHeaderNumber = ?1
            ORDER BY r.orderReferPosition
            """;

    private static final String NOTES_SQL = """
            SELECT n.orderNoteId, n.orderNotePosition, n.orderNoteText
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
        assertThat(dto.getOrderHeaderVendor()).isNotNull();
    }

    @Test
    void referencesMatchSql() {
        assertReferencesMatchSql(ORDER_NUMBER);
    }

    @Test
    void referencesWithProjectMatchSql() {
        List<?> numbers = em.createNativeQuery("""
                SELECT TOP 1 h.orderHeaderNumber FROM ErpDb.dbo.OrderHeader h
                JOIN ErpDb.dbo.OrderReference r ON r.orderReferOrderHeader = h.orderHeaderId
                WHERE r.orderReferProject IS NOT NULL
                """).getResultList();
        assertThat(numbers).as("pedido con lineas con proyecto").isNotEmpty();

        OrderDetailDto dto = assertReferencesMatchSql(((Number) numbers.get(0)).intValue());
        assertThat(dto.getOrderReference()).anySatisfy(line -> assertThat(line.getOrderReferProject()).isNotNull());
    }

    private OrderDetailDto assertReferencesMatchSql(int orderNumber) {
        OrderDetailDto dto = orderDetailService.findByOrderNumber(orderNumber).orElseThrow();
        List<Object[]> rows = rows(REFERENCES_SQL, orderNumber);

        assertThat(dto.getOrderReference()).hasSameSizeAs(rows).isNotEmpty();
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            OrderDetailReferenceDto line = dto.getOrderReference().get(i);
            ReferenceDto reference = line.getOrderReferReference();
            assertThat(line.getOrderReferId()).isEqualTo(((Number) row[0]).longValue());
            assertThat(line.getOrderReferPosition()).isEqualTo(((Number) row[1]).intValue());
            assertThat(reference.getReferId()).isEqualTo(((Number) row[2]).longValue());
            assertThat(reference.getReferCod()).isEqualTo(row[3]);
            assertThat(reference.getReferCod2()).isEqualTo(row[4]);
            assertThat(reference.getReferName()).isEqualTo(row[5]);
            assertThat(reference.getReferDescription()).isEqualTo(row[6]);
            assertThat(reference.getReferMeasuUnit().getMeasuUnitId()).isEqualTo(((Number) row[7]).longValue());
            assertThat(reference.getReferMeasuUnit().getMeasuUnitName()).isEqualTo(row[8]);
            assertThat(line.getOrderReferQuantity()).isEqualTo(((Number) row[9]).intValue());
            assertThat(line.getOrderReferUnitPrice()).isEqualByComparingTo((BigDecimal) row[10]);
            assertThat(line.getValorTotal()).isEqualByComparingTo((BigDecimal) row[11]);
            assertThat(line.getOrderReferDelivDate()).isEqualTo(((Date) row[12]).toLocalDate());
            assertThat(line.getOrderReferStatus().getOrderReferStatusId()).isEqualTo(((Number) row[13]).longValue());
            assertThat(line.getOrderReferStatus().getOrderReferStatusName()).isEqualTo(row[14]);
            if (row[15] == null) {
                assertThat(line.getOrderReferProject()).isNull();
            } else {
                assertThat(line.getOrderReferProject().getProjeId()).isEqualTo(((Number) row[15]).longValue());
                assertThat(line.getOrderReferProject().getProjeName()).isEqualTo(row[16]);
            }
        }
        return dto;
    }

    @Test
    void notesMatchSql() {
        OrderDetailDto dto = orderDetailService.findByOrderNumber(ORDER_NUMBER).orElseThrow();
        List<Object[]> rows = rows(NOTES_SQL, ORDER_NUMBER);

        assertThat(dto.getOrderNote()).extracting(OrderDetailNoteDto::getOrderNoteId)
                .containsExactlyElementsOf(rows.stream().map(r -> ((Number) r[0]).longValue()).toList());
        assertThat(dto.getOrderNote()).extracting(OrderDetailNoteDto::getOrderNotePosition)
                .containsExactlyElementsOf(rows.stream().map(r -> ((Number) r[1]).intValue()).toList());
        assertThat(dto.getOrderNote()).extracting(OrderDetailNoteDto::getOrderNoteText)
                .containsExactlyElementsOf(rows.stream().map(r -> (String) r[2]).toList());
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
        assertThat(page).allSatisfy(dto -> assertThat(dto.getOrderHeaderDate()).isAfterOrEqualTo(from));
        assertThat(page).extracting(OrderDetailDto::getOrderHeaderNumber).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void lastDaysDetailMatchesSingleOrderDetail() {
        List<OrderDetailDto> page = orderDetailService.findLastDays(90,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "orderHeaderNumber")));
        assertThat(page).as("pedidos en los ultimos 90 dias").isNotEmpty();

        for (OrderDetailDto fromList : page) {
            OrderDetailDto single = orderDetailService.findByOrderNumber(fromList.getOrderHeaderNumber()).orElseThrow();
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

        assertThat(dto.getOrderHeaderId()).isEqualTo(((Number) row[0]).longValue());
        assertThat(dto.getOrderHeaderNumber()).isEqualTo(((Number) row[1]).intValue());
        assertThat(dto.getOrderHeaderDate()).isEqualTo(((Date) row[2]).toLocalDate());
        assertThat(dto.getOrderHeaderPaymeConditions()).isEqualTo(row[3]);
        assertThat(dto.getOrderHeaderDescription()).isEqualTo(row[4]);

        BranchDto branch = dto.getOrderHeaderBranch();
        assertThat(branch.getBranchId()).isEqualTo(((Number) row[5]).longValue());
        assertThat(branch.getBranchCode()).isEqualTo(row[6]);
        assertThat(branch.getBranchCity()).isEqualTo(row[7]);
        assertThat(branch.getBranchAddress()).isEqualTo(row[8]);
        assertThat(branch.getBranchClient().getClientId()).isEqualTo(((Number) row[9]).longValue());
        ThirdPartyDto clientThirdParty = branch.getBranchClient().getClientThirdParty();
        assertThat(clientThirdParty.getThirdPartyId()).isEqualTo(((Number) row[10]).longValue());
        assertThat(clientThirdParty.getThirdPartyIdentNumber()).isEqualTo(((Number) row[11]).longValue());
        assertThat(clientThirdParty.getThirdPartyName()).isEqualTo(row[12]);

        VendorDto vendor = dto.getOrderHeaderVendor();
        if (row[13] == null) {
            assertThat(vendor).isNull();
        } else {
            assertThat(vendor.getVendorId()).isEqualTo(((Number) row[13]).longValue());
            assertThat(vendor.getVendorCode()).isEqualTo(row[14]);
            assertThat(vendor.getVendorThirdParty().getThirdPartyId()).isEqualTo(((Number) row[15]).longValue());
            assertThat(vendor.getVendorThirdParty().getThirdPartyName()).isEqualTo(row[16]);
        }
        return dto;
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(String sql, int orderNumber) {
        return em.createNativeQuery(sql).setParameter(1, orderNumber).getResultList();
    }
}
