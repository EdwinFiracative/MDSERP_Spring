package com.proelectricos.mdserp.emp001_inv.coinvnetomp.controller;

import com.proelectricos.mdserp.emp001_inv.coinvnetomp.CoInvNetoMp;
import com.proelectricos.mdserp.emp001_inv.coinvnetomp.dto.CoInvNetoMpDto;
import com.proelectricos.mdserp.emp001_inv.coinvnetomp.service.CoInvNetoMpService;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.CoInvNetoSql;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.dto.CoInvNetoSqlDto;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.service.CoInvNetoSqlService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inv-neto-mp")
@RequiredArgsConstructor
public class CoInvNetoMpController {

    private final CoInvNetoMpService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoInvNetoMpDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoInvNetoMpDto convertToDto(CoInvNetoMp entity) {
        return mapper.map(entity, CoInvNetoMpDto.class);
    }
}
