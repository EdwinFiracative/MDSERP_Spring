package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViCostosCargadosGarDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViCostosCargadosGar;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViCostosCargadosGarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vi-costos-cargados-gar")
@RequiredArgsConstructor
public class ViCostosCargadosGarController {

    private final ViCostosCargadosGarService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViCostosCargadosGarDto> findAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/op/{op}")
    public List<ViCostosCargadosGarDto> findByOP(@PathVariable Integer op) {
        return service.findByOP(op)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViCostosCargadosGarDto convertToDto(ViCostosCargadosGar entity) {
        return mapper.map(entity, ViCostosCargadosGarDto.class);
    }
}
