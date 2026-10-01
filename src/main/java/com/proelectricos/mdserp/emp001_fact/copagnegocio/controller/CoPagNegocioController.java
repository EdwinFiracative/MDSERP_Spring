package com.proelectricos.mdserp.emp001_fact.copagnegocio.controller;

import com.proelectricos.mdserp.emp001_fact.copagnegocio.CoPagNegocio;
import com.proelectricos.mdserp.emp001_fact.copagnegocio.dto.CoPagNegocioDto;
import com.proelectricos.mdserp.emp001_fact.copagnegocio.service.CoPagNegocioService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pag-negocio")
public class CoPagNegocioController {

    private final CoPagNegocioService coPagNegocioService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoPagNegocioDto> getCoPagNegocio() {
        var coPagNegocioList = StreamSupport
                .stream(coPagNegocioService.findAllCoPagNegocio().spliterator(), false)
                .collect(Collectors.toList());

        return coPagNegocioList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoPagNegocioDto convertToDto(CoPagNegocio entity) {
        return mapper.map(entity, CoPagNegocioDto.class);
    }
}



