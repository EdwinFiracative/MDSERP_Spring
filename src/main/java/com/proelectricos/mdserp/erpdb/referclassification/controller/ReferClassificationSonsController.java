package com.proelectricos.mdserp.erpdb.referclassification.controller;

import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import com.proelectricos.mdserp.erpdb.referclassification.dto.ReferClassificationSonsDto;
import com.proelectricos.mdserp.erpdb.referclassification.service.ReferClassificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/referclassificationsons")

public class ReferClassificationSonsController {

    private final ReferClassificationService ReferClassificationService;
    private final ModelMapper mapper;
    @PreAuthorize("hasAuthority('ROLE_GETALLREFERCLASSIFICATIONSONS')")
    @GetMapping
    public List<ReferClassificationSonsDto> getAllReferClassificationSons() {
        return ReferClassificationService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReferClassificationSonsDto convertToDto(ReferClassification entity) {
        return mapper.map(entity, ReferClassificationSonsDto.class);
    }
}