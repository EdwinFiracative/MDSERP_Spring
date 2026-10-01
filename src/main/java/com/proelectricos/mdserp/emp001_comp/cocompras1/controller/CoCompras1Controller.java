package com.proelectricos.mdserp.emp001_comp.cocompras1.controller;

import com.proelectricos.mdserp.emp001_comp.cocompras1.CoCompras1;
import com.proelectricos.mdserp.emp001_comp.cocompras1.dto.CoCompras1Dto;
import com.proelectricos.mdserp.emp001_comp.cocompras1.service.CoCompras1Service;
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



