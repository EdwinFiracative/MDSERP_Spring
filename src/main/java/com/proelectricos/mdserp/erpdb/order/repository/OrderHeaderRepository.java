package com.proelectricos.mdserp.erpdb.order.repository;

import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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

    // Pedidos con fecha entre from y to (ambas incluidas)
    @Query("select h from OrderHeader h "
            + "join fetch h.orderHeaderBranch b "
            + "join fetch b.branchClient c "
            + "join fetch c.clientThirdParty "
            + "left join fetch h.orderHeaderVendor hv "
            + "left join fetch hv.vendorThirdParty "
            + "left join fetch b.branchVendor bv "
            + "left join fetch bv.vendorThirdParty "
            + "where h.orderHeaderDate between :from and :to "
            + "order by h.orderHeaderNumber desc")
    List<OrderHeader> findDetailByOrderHeaderDateBetween(LocalDate from, LocalDate to);

    // Igual que el anterior, pero solo pedidos con al menos una linea en alguno de los estados indicados
    @Query("select h from OrderHeader h "
            + "join fetch h.orderHeaderBranch b "
            + "join fetch b.branchClient c "
            + "join fetch c.clientThirdParty "
            + "left join fetch h.orderHeaderVendor hv "
            + "left join fetch hv.vendorThirdParty "
            + "left join fetch b.branchVendor bv "
            + "left join fetch bv.vendorThirdParty "
            + "where h.orderHeaderDate between :from and :to "
            + "and exists (select 1 from OrderReference r "
            + "where r.orderReferOrderHeader = h and r.orderReferStatus.id in :statusIds) "
            + "order by h.orderHeaderNumber desc")
    List<OrderHeader> findDetailByOrderHeaderDateBetweenAndStatusIn(LocalDate from, LocalDate to, Collection<Long> statusIds);
}
