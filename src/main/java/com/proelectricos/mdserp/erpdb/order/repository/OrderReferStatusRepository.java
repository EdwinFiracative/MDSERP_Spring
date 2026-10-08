package com.proelectricos.mdserp.erpdb.order.repository;

import com.proelectricos.mdserp.erpdb.order.OrderReferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReferStatusRepository extends JpaRepository<OrderReferStatus, Long> {
}
