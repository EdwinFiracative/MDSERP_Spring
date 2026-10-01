package com.proelectricos.mdserp.emp001_fact.copedpendaprob.controller;

import com.proelectricos.mdserp.emp001_fact.copedpendaprob.CoPedPendAprob;
import com.proelectricos.mdserp.emp001_fact.copedpendaprob.dto.CoPedPendAprobDto;
import com.proelectricos.mdserp.emp001_fact.copedpendaprob.service.CoPedPendAprobService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ped-pend-aprob")
public class CoPedPendAprobController {

    private final CoPedPendAprobService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoPedPendAprobDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoPedPendAprobDto convertToDto(CoPedPendAprob entity) {
        return mapper.map(entity, CoPedPendAprobDto.class);
    }
}



