package com.proelectricos.mdserp.emp001_inv.viopfabricur.controller;

import com.proelectricos.mdserp.emp001_inv.viopfabricur.ViOpFabricur;
import com.proelectricos.mdserp.emp001_inv.viopfabricur.dto.ViOpFabricurDto;
import com.proelectricos.mdserp.emp001_inv.viopfabricur.service.ViOpFabricurService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/op-fabricur")
public class ViOpFabricurController {

    private final ViOpFabricurService viOpFabricurService;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViOpFabricurDto> getViOpFabricur() {
        return viOpFabricurService.findAllViOpFabricur()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViOpFabricurDto convertToDto(ViOpFabricur entity) {
        return mapper.map(entity, ViOpFabricurDto.class);
    }
}



