package com.proelectricos.mdserp.emp001_inv.cocosmastermonom.controller;

import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.CoCosMasterMoNom;
import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.dto.CoCosMasterMoNomDto;
import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.service.CoCosMasterMoNomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cos-master-mo-nom")
@RequiredArgsConstructor
public class CoCosMasterMoNomController {

    private final CoCosMasterMoNomService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoCosMasterMoNomDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoCosMasterMoNomDto convertToDto(CoCosMasterMoNom entity) {
        return mapper.map(entity, CoCosMasterMoNomDto.class);
    }
}
