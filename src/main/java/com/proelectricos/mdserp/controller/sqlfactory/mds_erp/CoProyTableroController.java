package com.proelectricos.mdserp.controller.sqlfactory.mds_erp;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp004_inv.AdicDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.mds_erp.CoProyTableroDto;

import com.proelectricos.mdserp.model.entity.sqlfactory.mds_erp.CoProyTablero;

import com.proelectricos.mdserp.service.sqlfactory.mds_erp.CoProyTableroService;
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



