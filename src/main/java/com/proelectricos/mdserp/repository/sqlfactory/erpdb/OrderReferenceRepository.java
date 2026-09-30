package com.proelectricos.mdserp.repository.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderReferenceRepository extends JpaRepository<OrderReference, Long> {

    @Query("select r from OrderReference r "
            + "join fetch r.orderReferReference ref "
            + "join fetch ref.referMeasuUnit "
            + "where r.orderReferOrderHeader.id = :orderHeaderId "
            + "order by r.orderReferPosition")
    List<OrderReference> findDetailByOrderHeaderId(Long orderHeaderId);

    @Query("select r from OrderReference r "
            + "join fetch r.orderReferReference ref "
            + "join fetch ref.referMeasuUnit "
            + "where r.orderReferOrderHeader.id in :orderHeaderIds "
            + "order by r.orderReferPosition")
    List<OrderReference> findDetailByOrderHeaderIdIn(Collection<Long> orderHeaderIds);
}
