package com.proelectricos.mdserp.controller.sqlfactory.emp004_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp004_inv.AdicDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv.Adic;
import com.proelectricos.mdserp.service.sqlfactory.emp004_inv.AdicService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@RestController
@RequestMapping("/api/adic")
class AdicController {
private final AdicService adicService;
private final ModelMapper mapper;

@GetMapping
    public List<AdicDto> getAdic() {
    var adicList = StreamSupport
            .stream(adicService.findAllAdics().spliterator(), false)
            .collect(Collectors.toList());

        return adicList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
     }

    private AdicDto convertToDto(Adic entity) {
        return mapper.map(entity, AdicDto.class);
    }

}



