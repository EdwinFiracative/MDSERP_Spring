package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.IndicadoresSeguimientoDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.IndicadoresSeguimiento;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.IndicadoresSeguimientoService;
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
