package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.PedidoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pedido")
class PedidoController {
    private final PedidoService PedidoService;
    private final ModelMapper mapper;

    @GetMapping
    public List<PedidoDto> getPedido() {
        return PedidoService.findAllPedidos()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PedidoDto convertToDto(Pedido entity) {
        return mapper.map(entity, PedidoDto.class);
    }

}
