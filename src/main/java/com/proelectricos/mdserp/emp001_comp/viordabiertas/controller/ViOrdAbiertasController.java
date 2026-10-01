package com.proelectricos.mdserp.emp001_comp.viordabiertas.controller;

import com.proelectricos.mdserp.emp001_comp.viordabiertas.ViOrdAbiertas;
import com.proelectricos.mdserp.emp001_comp.viordabiertas.dto.ViOrdAbiertasDto;
import com.proelectricos.mdserp.emp001_comp.viordabiertas.service.ViOrdAbiertasService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ord-abiertas")
@RequiredArgsConstructor
public class ViOrdAbiertasController {

    private final ViOrdAbiertasService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViOrdAbiertasDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViOrdAbiertasDto convertToDto(ViOrdAbiertas entity) {
        return mapper.map(entity, ViOrdAbiertasDto.class);
    }
}
