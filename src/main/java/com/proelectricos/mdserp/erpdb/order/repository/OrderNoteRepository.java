package com.proelectricos.mdserp.erpdb.order.repository;

import com.proelectricos.mdserp.erpdb.order.OrderNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderNoteRepository extends JpaRepository<OrderNote, Long> {

    List<OrderNote> findByOrderNoteOrderHeader_IdOrderByOrderNotePosition(Long orderHeaderId);

    // Notas de los pedidos con fecha entre from y to (ambas incluidas)
    List<OrderNote> findByOrderNoteOrderHeader_OrderHeaderDateBetweenOrderByOrderNotePosition(LocalDate from, LocalDate to);

    // Igual que el anterior, pero solo de pedidos con al menos una linea en alguno de los estados indicados
    @Query("select n from OrderNote n "
            + "where n.orderNoteOrderHeader.orderHeaderDate between :from and :to "
            + "and exists (select 1 from OrderReference r "
            + "where r.orderReferOrderHeader = n.orderNoteOrderHeader and r.orderReferStatus.id in :statusIds) "
            + "order by n.orderNotePosition")
    List<OrderNote> findByOrderHeaderDateBetweenAndStatusIn(LocalDate from, LocalDate to, Collection<Long> statusIds);
}
