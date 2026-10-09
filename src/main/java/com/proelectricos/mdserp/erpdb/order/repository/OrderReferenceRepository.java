package com.proelectricos.mdserp.erpdb.order.repository;

import com.proelectricos.mdserp.erpdb.order.OrderReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderReferenceRepository extends JpaRepository<OrderReference, Long> {

    @Query("select r from OrderReference r "
            + "join fetch r.orderReferReference ref "
            + "join fetch ref.referMeasuUnit "
            + "join fetch r.orderReferStatus "
            + "left join fetch r.orderReferProject "
            + "where r.orderReferOrderHeader.id = :orderHeaderId "
            + "order by r.orderReferPosition")
    List<OrderReference> findDetailByOrderHeaderId(Long orderHeaderId);

    @Query("select r from OrderReference r "
            + "join fetch r.orderReferReference ref "
            + "join fetch ref.referMeasuUnit "
            + "join fetch r.orderReferStatus "
            + "left join fetch r.orderReferProject "
            + "where r.orderReferOrderHeader.orderHeaderDate between :from and :to "
            + "order by r.orderReferPosition")
    List<OrderReference> findDetailByOrderHeaderDateBetween(LocalDate from, LocalDate to);

    @Query("select r from OrderReference r "
            + "join fetch r.orderReferReference ref "
            + "join fetch ref.referMeasuUnit "
            + "join fetch r.orderReferStatus "
            + "left join fetch r.orderReferProject "
            + "where r.orderReferOrderHeader.orderHeaderDate between :from and :to "
            + "and r.orderReferStatus.id in :statusIds "
            + "order by r.orderReferPosition")
    List<OrderReference> findDetailByOrderHeaderDateBetweenAndStatusIn(LocalDate from, LocalDate to, Collection<Long> statusIds);
}
