package com.proelectricos.mdserp.controller;

import com.proelectricos.mdserp.model.dto.sqlfactory.CoPedidosPendLaDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.CoPedidosPendLa;
import com.proelectricos.mdserp.service.sqlfactory.CoPedidosPendLaService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pedidos-pend")
public class CoPedidosPendLaController {

    private final CoPedidosPendLaService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoPedidosPendLaDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoPedidosPendLaDto convertToDto(CoPedidosPendLa entity) {
        return mapper.map(entity, CoPedidosPendLaDto.class);
    }
}
