package com.proelectricos.mdserp.emp001_inv.dpto.controller;

import com.proelectricos.mdserp.emp001_inv.dpto.Dpto;
import com.proelectricos.mdserp.emp001_inv.dpto.dto.DptoDto;
import com.proelectricos.mdserp.emp001_inv.dpto.service.DptoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departamentos")
public class DptoController {

    private final DptoService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<DptoDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DptoDto convertToDto(Dpto entity) {
        return mapper.map(entity, DptoDto.class);
    }
}



