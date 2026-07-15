package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.CoInvNetoSqlDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoSql;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.CoInvNetoSqlService;
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



