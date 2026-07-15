package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.CoInvNetoMpDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoMp;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.CoInvNetoMpService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/inv-neto-mp")
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



