package com.proelectricos.mdserp.controller.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp.ViOrdAbiertasDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.ViOrdAbiertas;
import com.proelectricos.mdserp.service.sqlfactory.emp001_comp.ViOrdAbiertasService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ord-abiertas")
@RequiredArgsConstructor
public class ViOrdAbiertasController {

    private final ViOrdAbiertasService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViOrdAbiertasDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViOrdAbiertasDto convertToDto(ViOrdAbiertas entity) {
        return mapper.map(entity, ViOrdAbiertasDto.class);
    }
}
