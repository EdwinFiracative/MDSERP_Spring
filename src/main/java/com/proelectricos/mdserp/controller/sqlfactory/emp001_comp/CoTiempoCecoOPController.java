package com.proelectricos.mdserp.controller.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp.CoTiempoCecoOPDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.CoTiempoCecoOP;
import com.proelectricos.mdserp.service.sqlfactory.emp001_comp.CoTiempoCecoOPService;
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
