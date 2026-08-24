package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViTimeMastSemieOpDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViTimeMastSemieOp;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViTimeMastSemieOpService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-mast-semie-op")
@RequiredArgsConstructor
public class ViTimeMastSemieOpController {

    private final ViTimeMastSemieOpService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViTimeMastSemieOpDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViTimeMastSemieOpDto convertToDto(ViTimeMastSemieOp entity) {
        return mapper.map(entity, ViTimeMastSemieOpDto.class);
    }
}
