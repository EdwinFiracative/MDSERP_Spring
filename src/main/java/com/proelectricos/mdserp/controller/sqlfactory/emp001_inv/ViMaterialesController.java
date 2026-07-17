package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViMaterialesDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMateriales;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViMaterialesService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<ViMaterialesDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        int cappedSize = Math.min(Math.max(size, 1), 500);
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), cappedSize, Sort.by("id").ascending());

        return service.findAll(pageRequest)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViMaterialesDto convertToDto(ViMateriales entity) {
        return mapper.map(entity, ViMaterialesDto.class);
    }
}


