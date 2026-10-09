package com.proelectricos.mdserp.erpdb.order.service;

import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import com.proelectricos.mdserp.erpdb.order.OrderNote;
import com.proelectricos.mdserp.erpdb.order.OrderReference;
import com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto;
import com.proelectricos.mdserp.erpdb.order.mapper.OrderHeaderMapper;
import com.proelectricos.mdserp.erpdb.order.repository.OrderHeaderRepository;
import com.proelectricos.mdserp.erpdb.order.repository.OrderNoteRepository;
import com.proelectricos.mdserp.erpdb.order.repository.OrderReferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderHeaderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderReferenceRepository orderReferenceRepository;
    private final OrderNoteRepository orderNoteRepository;
    private final OrderHeaderMapper orderHeaderMapper;

    public Optional<OrderHeaderDto> findByOrderNumber(Integer orderNumber) {
        return orderHeaderRepository.findDetailByOrderHeaderNumber(orderNumber)
                .map(header -> orderHeaderMapper.toDto(header,
                        orderReferenceRepository.findDetailByOrderHeaderId(header.getId()),
                        orderNoteRepository.findByOrderNoteOrderHeader_IdOrderByOrderNotePosition(header.getId())));
    }

    // Pedidos con fecha entre from y to (ambas incluidas). Si statusIds trae valores, solo pedidos
    // con lineas en esos estados y solo esas lineas. Lineas y notas se cargan en bloque filtrando por el mismo
    // rango de fechas (no por lista de ids: SQL Server admite maximo 2100 parametros por consulta).
    public List<OrderHeaderDto> findByDateRange(LocalDate from, LocalDate to, Collection<Long> statusIds) {
        boolean filterStatus = statusIds != null && !statusIds.isEmpty();
        List<OrderHeader> headers = filterStatus
                ? orderHeaderRepository.findDetailByOrderHeaderDateBetweenAndStatusIn(from, to, statusIds)
                : orderHeaderRepository.findDetailByOrderHeaderDateBetween(from, to);
        if (headers.isEmpty()) {
            return List.of();
        }
        List<OrderReference> lines = filterStatus
                ? orderReferenceRepository.findDetailByOrderHeaderDateBetweenAndStatusIn(from, to, statusIds)
                : orderReferenceRepository.findDetailByOrderHeaderDateBetween(from, to);
        Map<Long, List<OrderReference>> linesByHeader = lines.stream()
                .collect(Collectors.groupingBy(line -> line.getOrderReferOrderHeader().getId()));
        List<OrderNote> notes = filterStatus
                ? orderNoteRepository.findByOrderHeaderDateBetweenAndStatusIn(from, to, statusIds)
                : orderNoteRepository.findByOrderNoteOrderHeader_OrderHeaderDateBetweenOrderByOrderNotePosition(from, to);
        Map<Long, List<OrderNote>> notesByHeader = notes.stream()
                .collect(Collectors.groupingBy(note -> note.getOrderNoteOrderHeader().getId()));

        return headers.stream()
                .map(header -> orderHeaderMapper.toDto(header,
                        linesByHeader.getOrDefault(header.getId(), List.of()),
                        notesByHeader.getOrDefault(header.getId(), List.of())))
                .toList();
    }
}
