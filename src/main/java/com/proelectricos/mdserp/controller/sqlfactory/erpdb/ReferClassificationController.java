package com.proelectricos.mdserp.controller.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.ReferClassificationDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification;
import com.proelectricos.mdserp.service.sqlfactory.erpdb.ReferClassificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/referclassification")
public class ReferClassificationController {

    private final ReferClassificationService ReferClassificationService;
    private final ModelMapper mapper;

    @GetMapping
    public List<ReferClassificationDto> getAllReferClassification() {
        return ReferClassificationService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReferClassificationDto convertToDto(ReferClassification entity) {
        return mapper.map(entity, ReferClassificationDto.class);
    }
}