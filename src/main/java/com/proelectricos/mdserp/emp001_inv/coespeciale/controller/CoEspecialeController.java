package com.proelectricos.mdserp.emp001_inv.coespeciale.controller;

import com.proelectricos.mdserp.emp001_inv.coespeciale.CoEspeciale;
import com.proelectricos.mdserp.emp001_inv.coespeciale.dto.CoEspecialeDto;
import com.proelectricos.mdserp.emp001_inv.coespeciale.service.CoEspecialeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/especiales")
public class CoEspecialeController {

    private final CoEspecialeService coEspecialeService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoEspecialeDto> getCoEspeciales() {
        return coEspecialeService.findAllCoEspeciales()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoEspecialeDto convertToDto(CoEspeciale entity) {
        return mapper.map(entity, CoEspecialeDto.class);
    }
}



