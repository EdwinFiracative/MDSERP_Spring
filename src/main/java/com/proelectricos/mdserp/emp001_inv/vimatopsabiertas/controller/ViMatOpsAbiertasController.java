package com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.controller;

import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.ViMatOpsAbiertas;
import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.dto.ViMatOpsAbiertasDto;
import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.service.ViMatOpsAbiertasService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi-mat-ops-abiertas")
public class ViMatOpsAbiertasController {

    private final ViMatOpsAbiertasService service;
    private final ModelMapper mapper;

    /** GET /api/vi-mat-ops-abiertas?anio=2026 (anio opcional; por defecto el año en curso). */
    @GetMapping
    public List<ViMatOpsAbiertasDto> getAll(@RequestParam(required = false) Integer anio) {
        return service.findAll(anio)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** GET /api/vi-mat-ops-abiertas/op/{op}?anio=2026 */
    @GetMapping("/op/{op}")
    public List<ViMatOpsAbiertasDto> findByOp(@PathVariable Integer op,
                                              @RequestParam(required = false) Integer anio) {
        return service.findByOp(op, anio)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViMatOpsAbiertasDto convertToDto(ViMatOpsAbiertas entity) {
        return mapper.map(entity, ViMatOpsAbiertasDto.class);
    }
}
