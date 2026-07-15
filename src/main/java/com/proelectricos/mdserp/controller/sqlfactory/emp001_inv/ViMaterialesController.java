package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViMaterialesDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMateriales;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViMaterialesService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi-materiales")
public class ViMaterialesController {

    private final ViMaterialesService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViMaterialesDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViMaterialesDto convertToDto(ViMateriales entity) {
        return mapper.map(entity, ViMaterialesDto.class);
    }
}



