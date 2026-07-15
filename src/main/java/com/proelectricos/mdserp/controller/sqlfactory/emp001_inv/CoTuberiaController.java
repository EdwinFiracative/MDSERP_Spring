package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.CoTuberiaDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoTuberia;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.CoTuberiaService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tuberia")
public class CoTuberiaController {

    private final CoTuberiaService coTuberiaService;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoTuberiaDto> getCoTuberia() {
        return coTuberiaService.findAllCoTuberia()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoTuberiaDto convertToDto(CoTuberia entity) {
        return mapper.map(entity, CoTuberiaDto.class);
    }
}



