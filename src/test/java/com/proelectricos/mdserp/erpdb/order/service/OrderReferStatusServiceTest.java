package com.proelectricos.mdserp.erpdb.order.service;

import com.proelectricos.mdserp.erpdb.order.dto.OrderReferStatusDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Compara los estados de linea de pedido contra la tabla OrderReferStatus (solo lectura).
 */
@SpringBootTest
@Transactional(readOnly = true)
class OrderReferStatusServiceTest {

    @Autowired
    private OrderReferStatusService orderReferStatusService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void findAllReturnsEveryStatusOrderedById() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(
                "SELECT orderReferStatusId, orderReferStatusName FROM ErpDb.dbo.OrderReferStatus ORDER BY orderReferStatusId")
                .getResultList();

        List<OrderReferStatusDto> statuses = orderReferStatusService.findAll();

        assertThat(statuses).hasSize(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertThat(statuses.get(i).getOrderReferStatusId()).isEqualTo(((Number) rows.get(i)[0]).longValue());
            assertThat(statuses.get(i).getOrderReferStatusName()).isEqualTo(rows.get(i)[1]);
        }
    }
}
