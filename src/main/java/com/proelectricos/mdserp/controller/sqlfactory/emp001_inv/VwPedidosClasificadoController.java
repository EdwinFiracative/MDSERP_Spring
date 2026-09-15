package com.proelectricos.mdserp.controller.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.VwPedidosClasificadoDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.VwPedidosClasificado;
import com.proelectricos.mdserp.service.sqlfactory.emp001_inv.VwPedidosClasificadoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos-clasificados")
@RequiredArgsConstructor
public class VwPedidosClasificadoController {

    private final VwPedidosClasificadoService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<VwPedidosClasificadoDto> getAll() {
        return service.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private VwPedidosClasificadoDto convertToDto(VwPedidosClasificado entity) {
        return mapper.map(entity, VwPedidosClasificadoDto.class);
    }
}
