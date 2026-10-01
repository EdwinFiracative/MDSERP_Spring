package com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.controller;

import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.IndicadoresSeguimiento;
import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.dto.IndicadoresSeguimientoDto;
import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.service.IndicadoresSeguimientoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/indicadores-seguimiento")
@RequiredArgsConstructor
public class IndicadoresSeguimientoController {

    private final IndicadoresSeguimientoService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<IndicadoresSeguimientoDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private IndicadoresSeguimientoDto convertToDto(IndicadoresSeguimiento entity) {
        return mapper.map(entity, IndicadoresSeguimientoDto.class);
    }
}
