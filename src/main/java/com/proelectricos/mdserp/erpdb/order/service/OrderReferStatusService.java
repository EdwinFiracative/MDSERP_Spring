package com.proelectricos.mdserp.erpdb.order.service;

import com.proelectricos.mdserp.erpdb.order.dto.OrderReferStatusDto;
import com.proelectricos.mdserp.erpdb.order.mapper.OrderHeaderMapper;
import com.proelectricos.mdserp.erpdb.order.repository.OrderReferStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderReferStatusService {

    private final OrderReferStatusRepository orderReferStatusRepository;
    private final OrderHeaderMapper orderHeaderMapper;

    public List<OrderReferStatusDto> findAll() {
        return orderReferStatusRepository.findAll(Sort.by("id")).stream()
                .map(orderHeaderMapper::toDto)
                .toList();
    }
}
