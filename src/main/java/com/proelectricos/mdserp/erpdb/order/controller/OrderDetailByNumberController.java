package com.proelectricos.mdserp.erpdb.order.controller;

import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orderdetailbynumber")
public class OrderDetailByNumberController {
    private final OrderDetailService OrderDetailService;

    @GetMapping("/{num}")
    public ResponseEntity<OrderDetailDto> getOrderDetailByNumber(@PathVariable Integer num) {
        return OrderDetailService.findByOrderNumber(num)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
