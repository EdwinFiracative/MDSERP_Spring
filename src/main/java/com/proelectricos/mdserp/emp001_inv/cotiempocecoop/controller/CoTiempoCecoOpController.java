package com.proelectricos.mdserp.emp001_inv.cotiempocecoop.controller;

import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.CoTiempoCecoOp;
import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.dto.CoTiempoCecoOpDto;
import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.service.CoTiempoCecoOpService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tiempo-ceco-op-inv")
@RequiredArgsConstructor
public class CoTiempoCecoOpController {

    private final CoTiempoCecoOpService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoTiempoCecoOpDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoTiempoCecoOpDto convertToDto(CoTiempoCecoOp entity) {
        return mapper.map(entity, CoTiempoCecoOpDto.class);
    }
}
