package com.proelectricos.mdserp.repository.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderNoteRepository extends JpaRepository<OrderNote, Long> {

    List<OrderNote> findByOrderNoteOrderHeader_IdOrderByOrderNotePosition(Long orderHeaderId);

    List<OrderNote> findByOrderNoteOrderHeader_IdInOrderByOrderNotePosition(Collection<Long> orderHeaderIds);
}
