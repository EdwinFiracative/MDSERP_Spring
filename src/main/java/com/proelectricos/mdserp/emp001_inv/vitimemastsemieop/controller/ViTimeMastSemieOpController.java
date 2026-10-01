package com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.controller;

import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.ViTimeMastSemieOp;
import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.dto.ViTimeMastSemieOpDto;
import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.service.ViTimeMastSemieOpService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-mast-semie-op")
@RequiredArgsConstructor
public class ViTimeMastSemieOpController {

    private final ViTimeMastSemieOpService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViTimeMastSemieOpDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViTimeMastSemieOpDto convertToDto(ViTimeMastSemieOp entity) {
        return mapper.map(entity, ViTimeMastSemieOpDto.class);
    }
}
