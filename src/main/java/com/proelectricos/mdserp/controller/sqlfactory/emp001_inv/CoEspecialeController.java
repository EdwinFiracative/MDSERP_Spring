package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.CoEspecialeDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoEspeciale;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.CoEspecialeService;
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



