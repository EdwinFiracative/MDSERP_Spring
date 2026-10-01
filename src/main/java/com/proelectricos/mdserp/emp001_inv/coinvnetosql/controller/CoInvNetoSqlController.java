package com.proelectricos.mdserp.emp001_inv.coinvnetosql.controller;

import com.proelectricos.mdserp.emp001_inv.coinvnetosql.CoInvNetoSql;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.dto.CoInvNetoSqlDto;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.service.CoInvNetoSqlService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/inv-neto-sql")
public class CoInvNetoSqlController {

    private final CoInvNetoSqlService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoInvNetoSqlDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoInvNetoSqlDto convertToDto(CoInvNetoSql entity) {
        return mapper.map(entity, CoInvNetoSqlDto.class);
    }
}



