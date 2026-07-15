package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.CoPagosCarDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagosCar;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.CoPagosCarService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pagos-car")
public class CoPagosCarController {

    private final CoPagosCarService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<CoPagosCarDto> getAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoPagosCarDto convertToDto(CoPagosCar entity) {
        return mapper.map(entity, CoPagosCarDto.class);
    }
}



