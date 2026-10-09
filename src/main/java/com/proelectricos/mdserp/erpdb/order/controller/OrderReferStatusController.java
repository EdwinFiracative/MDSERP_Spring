package com.proelectricos.mdserp.erpdb.order.controller;

import com.proelectricos.mdserp.erpdb.order.dto.OrderReferStatusDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderReferStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orderreferstatus")
public class OrderReferStatusController {
    private final OrderReferStatusService orderReferStatusService;

    // Estados de linea de pedido; sus ids son los que acepta el parametro "estados" de /api/orderheader.
    @GetMapping
    public List<OrderReferStatusDto> getOrderReferStatuses() {
        return orderReferStatusService.findAll();
    }
}
