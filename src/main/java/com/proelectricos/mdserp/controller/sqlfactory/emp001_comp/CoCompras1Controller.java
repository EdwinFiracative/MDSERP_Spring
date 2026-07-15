package com.proelectricos.mdserp.controller.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp.CoCompras1Dto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.CoCompras1;
import com.proelectricos.mdserp.service.sqlfactory.emp001_comp.CoCompras1Service;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/compras")
public class CoCompras1Controller {

    private final CoCompras1Service service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoCompras1Dto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoCompras1Dto convertToDto(CoCompras1 entity) {
        return mapper.map(entity, CoCompras1Dto.class);
    }
}



