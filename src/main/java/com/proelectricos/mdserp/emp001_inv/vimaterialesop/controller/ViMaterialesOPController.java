package com.proelectricos.mdserp.emp001_inv.vimaterialesop.controller;

import com.proelectricos.mdserp.emp001_inv.vimaterialesop.ViMaterialesOP;
import com.proelectricos.mdserp.emp001_inv.vimaterialesop.dto.ViMaterialesOPDto;
import com.proelectricos.mdserp.emp001_inv.vimaterialesop.service.ViMaterialesOPService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi-materiales-op")
public class ViMaterialesOPController {

    private final ViMaterialesOPService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViMaterialesOPDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/op/{op}")
    public List<ViMaterialesOPDto> findByOP(@PathVariable Integer op) {
        return service.findByOP(op)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViMaterialesOPDto convertToDto(ViMaterialesOP entity) {
        return mapper.map(entity, ViMaterialesOPDto.class);
    }
}


