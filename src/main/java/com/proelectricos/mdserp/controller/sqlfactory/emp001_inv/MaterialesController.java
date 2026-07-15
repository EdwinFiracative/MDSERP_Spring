package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.MaterialesDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Materiales;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.MaterialesService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/materiales")
public class MaterialesController {

    private final MaterialesService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<MaterialesDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MaterialesDto convertToDto(Materiales entity) {
        return mapper.map(entity, MaterialesDto.class);
    }
}



