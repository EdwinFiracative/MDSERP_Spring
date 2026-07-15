package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.CoCarteraNegocioDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoCarteraNegocio;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.CoCarteraNegocioService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cartera-negocio")
public class CoCarteraNegocioController {

    private final CoCarteraNegocioService coCarteraNegocioService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoCarteraNegocioDto> getCoCarteraNegocio() {
        return coCarteraNegocioService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoCarteraNegocioDto convertToDto(CoCarteraNegocio entity) {
        return mapper.map(entity, CoCarteraNegocioDto.class);
    }
}



