package com.proelectricos.mdserp.emp001_comp.vimrp.controller;

import com.proelectricos.mdserp.emp001_comp.vimrp.ViMrp;
import com.proelectricos.mdserp.emp001_comp.vimrp.dto.ViMrpDto;
import com.proelectricos.mdserp.emp001_comp.vimrp.service.ViMrpService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi-mrp")
public class ViMrpController {

    private final ViMrpService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViMrpDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViMrpDto convertToDto(ViMrp entity) {
        return mapper.map(entity, ViMrpDto.class);
    }
}
