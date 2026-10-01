package com.proelectricos.mdserp.mds_erp.coproytablero.controller;

import com.proelectricos.mdserp.emp004_inv.adic.dto.AdicDto;
import com.proelectricos.mdserp.mds_erp.coproytablero.CoProyTablero;
import com.proelectricos.mdserp.mds_erp.coproytablero.dto.CoProyTableroDto;
import com.proelectricos.mdserp.mds_erp.coproytablero.service.CoProyTableroService;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@AllArgsConstructor
@RestController
@RequestMapping("/api/proytableros")
class CoProyTableroController {
    private final CoProyTableroService CoProyTableroService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoProyTableroDto> getCoProyTablero() {
        var CoProyTableroList = StreamSupport
                .stream(CoProyTableroService.findAllCoProyTablero().spliterator(), false)
                .collect(Collectors.toList());

        return CoProyTableroList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoProyTableroDto convertToDto(CoProyTablero entity) {
        return mapper.map(entity, CoProyTableroDto.class);
    }

}



