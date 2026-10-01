package com.proelectricos.mdserp.erpdb.order.service;

import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import com.proelectricos.mdserp.erpdb.order.OrderNote;
import com.proelectricos.mdserp.erpdb.order.OrderReference;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailDto;
import com.proelectricos.mdserp.erpdb.order.mapper.OrderDetailMapper;
import com.proelectricos.mdserp.erpdb.order.repository.OrderHeaderRepository;
import com.proelectricos.mdserp.erpdb.order.repository.OrderNoteRepository;
import com.proelectricos.mdserp.erpdb.order.repository.OrderReferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderDetailService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderReferenceRepository orderReferenceRepository;
    private final OrderNoteRepository orderNoteRepository;
    private final OrderDetailMapper orderDetailMapper;

    public Optional<OrderDetailDto> findByOrderNumber(Integer orderNumber) {
        return orderHeaderRepository.findDetailByOrderHeaderNumber(orderNumber)
                .map(header -> orderDetailMapper.toDto(header,
                        orderReferenceRepository.findDetailByOrderHeaderId(header.getId()),
                        orderNoteRepository.findByOrderNoteOrderHeader_IdOrderByOrderNotePosition(header.getId())));
    }

    // Pedidos con fecha dentro de los ultimos "days" dias; lineas y notas se cargan en bloque para toda la pagina
    public List<OrderDetailDto> findLastDays(int days, Pageable pageable) {
        List<OrderHeader> headers = orderHeaderRepository
                .findDetailByOrderHeaderDateFrom(LocalDate.now().minusDays(days), pageable)
                .getContent();
        if (headers.isEmpty()) {
            return List.of();
        }
        List<Long> ids = headers.stream().map(OrderHeader::getId).toList();
        Map<Long, List<OrderReference>> linesByHeader = orderReferenceRepository.findDetailByOrderHeaderIdIn(ids).stream()
                .collect(Collectors.groupingBy(line -> line.getOrderReferOrderHeader().getId()));
        Map<Long, List<OrderNote>> notesByHeader = orderNoteRepository.findByOrderNoteOrderHeader_IdInOrderByOrderNotePosition(ids).stream()
                .collect(Collectors.groupingBy(note -> note.getOrderNoteOrderHeader().getId()));

        return headers.stream()
                .map(header -> orderDetailMapper.toDto(header,
                        linesByHeader.getOrDefault(header.getId(), List.of()),
                        notesByHeader.getOrDefault(header.getId(), List.of())))
                .toList();
    }
}
