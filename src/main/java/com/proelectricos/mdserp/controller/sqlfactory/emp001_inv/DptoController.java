package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.DptoDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Dpto;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.DptoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departamentos")
public class DptoController {

    private final DptoService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<DptoDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DptoDto convertToDto(Dpto entity) {
        return mapper.map(entity, DptoDto.class);
    }
}



