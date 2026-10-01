package com.proelectricos.mdserp.erpdb.referclassification.controller;

import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import com.proelectricos.mdserp.erpdb.referclassification.dto.ReferClassificationDto;
import com.proelectricos.mdserp.erpdb.referclassification.service.ReferClassificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_GETALLREFERCLASSIFICATION')")
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