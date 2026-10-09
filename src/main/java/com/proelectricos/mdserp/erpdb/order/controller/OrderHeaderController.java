package com.proelectricos.mdserp.erpdb.order.controller;

import com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderHeaderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orderheader")
public class OrderHeaderController {
    private final OrderHeaderService orderHeaderService;

    // Pedidos con fecha de encabezado entre fechaInicial y fechaFinal (ambas incluidas).
    // "estados" (opcional) son ids de OrderReferStatus: solo pedidos con lineas en esos estados y solo esas lineas.
    // Ej: /api/orderheader?fechaInicial=2026-01-01&fechaFinal=2026-01-31&estados=1,2
    @GetMapping
    public List<OrderHeaderDto> getOrderHeaders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal,
            @RequestParam(required = false) List<Long> estados
    ) {
        if (fechaInicial.isAfter(fechaFinal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInicial no puede ser mayor que fechaFinal");
        }
        return orderHeaderService.findByDateRange(fechaInicial, fechaFinal, estados);
    }

    @GetMapping("/{num}")
    public ResponseEntity<OrderHeaderDto> getOrderHeaderByNumber(@PathVariable Integer num) {
        return orderHeaderService.findByOrderNumber(num)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
