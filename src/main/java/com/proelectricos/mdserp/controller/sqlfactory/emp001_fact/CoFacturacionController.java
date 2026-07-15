package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.dto.sqlfactory.emp004_inv.AdicDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.CoFacturacionDto;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoFacturacion;

import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.CoFacturacionService;
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
@RequestMapping("/api/facturacion")
class CoFacturacionController {
    private final CoFacturacionService CoFacturacionService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoFacturacionDto> getCoFacturacion() {
        var CoFacturacionList = StreamSupport
                .stream(CoFacturacionService.findAllCoFacturacion().spliterator(), false)
                .collect(Collectors.toList());

        return CoFacturacionList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoFacturacionDto convertToDto(CoFacturacion entity) {
        return mapper.map(entity, CoFacturacionDto.class);
    }

}



