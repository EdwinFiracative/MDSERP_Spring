package com.proelectricos.mdserp.emp001_comp.cotiempocecoop.controller;

import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.CoTiempoCecoOP;
import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.dto.CoTiempoCecoOPDto;
import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.service.CoTiempoCecoOPService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tiempo-ceco-op")
@RequiredArgsConstructor
public class CoTiempoCecoOPController {

    private final CoTiempoCecoOPService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoTiempoCecoOPDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoTiempoCecoOPDto convertToDto(CoTiempoCecoOP entity) {
        return mapper.map(entity, CoTiempoCecoOPDto.class);
    }
}
