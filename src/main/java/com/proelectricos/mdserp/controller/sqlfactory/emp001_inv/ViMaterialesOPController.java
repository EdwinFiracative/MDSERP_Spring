package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViMaterialesOPDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMaterialesOP;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViMaterialesOPService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
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

    private ViMaterialesOPDto convertToDto(ViMaterialesOP entity) {
        return mapper.map(entity, ViMaterialesOPDto.class);
    }
}



