package com.proelectricos.mdserp.emp001_fact.cocartera.controller;

import com.proelectricos.mdserp.emp001_fact.cocartera.CoCartera;
import com.proelectricos.mdserp.emp001_fact.cocartera.dto.CoCarteraDto;
import com.proelectricos.mdserp.emp001_fact.cocartera.service.CoCarteraService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cartera")
public class CoCarteraController {

    private final CoCarteraService coCarteraService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoCarteraDto> getCoCartera() {
        return coCarteraService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoCarteraDto convertToDto(CoCartera entity) {
        return mapper.map(entity, CoCarteraDto.class);
    }
}



