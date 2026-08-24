package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ViOpMasterTimeCecoDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViOpMasterTimeCeco;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.ViOpMasterTimeCecoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/op-master-time-ceco")
@RequiredArgsConstructor
public class ViOpMasterTimeCecoController {

    private final ViOpMasterTimeCecoService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViOpMasterTimeCecoDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ViOpMasterTimeCecoDto convertToDto(ViOpMasterTimeCeco entity) {
        return mapper.map(entity, ViOpMasterTimeCecoDto.class);
    }
}
