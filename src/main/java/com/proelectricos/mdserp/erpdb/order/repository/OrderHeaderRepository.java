package com.proelectricos.mdserp.erpdb.order.repository;

import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {

    // Encabezado con sede, cliente y vendedores (del pedido y de la sede) en una sola consulta
    @Query("select h from OrderHeader h "
            + "join fetch h.orderHeaderBranch b "
            + "join fetch b.branchClient c "
            + "join fetch c.clientThirdParty "
            + "left join fetch h.orderHeaderVendor hv "
            + "left join fetch hv.vendorThirdParty "
            + "left join fetch b.branchVendor bv "
            + "left join fetch bv.vendorThirdParty "
            + "where h.orderHeaderNumber = :number")
    Optional<OrderHeader> findDetailByOrderHeaderNumber(Integer number);

    @Query(value = "select h from OrderHeader h "
            + "join fetch h.orderHeaderBranch b "
            + "join fetch b.branchClient c "
            + "join fetch c.clientThirdParty "
            + "left join fetch h.orderHeaderVendor hv "
            + "left join fetch hv.vendorThirdParty "
            + "left join fetch b.branchVendor bv "
            + "left join fetch bv.vendorThirdParty "
            + "where h.orderHeaderDate >= :from",
            countQuery = "select count(h) from OrderHeader h where h.orderHeaderDate >= :from")
    Page<OrderHeader> findDetailByOrderHeaderDateFrom(LocalDate from, Pageable pageable);
}
