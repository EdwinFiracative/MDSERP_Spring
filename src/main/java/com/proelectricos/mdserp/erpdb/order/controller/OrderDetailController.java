package com.proelectricos.mdserp.erpdb.order.controller;

import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orderdetail")
public class OrderDetailController {
    private final OrderDetailService OrderDetailService;

    // Pedidos de los ultimos "dias" dias (por defecto 90) con referencias y notas.
    // El orden usa los campos de la entidad OrderHeader (ej. orderHeaderNumber, orderHeaderDate).
    @GetMapping
    public List<OrderDetailDto> getOrderDetailLastDays(
            @RequestParam(defaultValue = "90") int dias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "orderHeaderNumber,desc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = sortParts.length > 1
                ? Sort.Direction.fromOptionalString(sortParts[1].trim()).orElse(Sort.Direction.ASC)
                : Sort.Direction.ASC;

        int cappedSize = Math.min(Math.max(size, 1), 1000);
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), cappedSize, Sort.by(direction, sortField));

        return OrderDetailService.findLastDays(Math.max(dias, 0), pageRequest);
    }
}
