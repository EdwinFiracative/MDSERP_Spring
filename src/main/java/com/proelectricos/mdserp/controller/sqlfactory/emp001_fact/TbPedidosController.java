package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.TbPedidosResumenDto;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.TbPedidosService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class TbPedidosController {

    private final TbPedidosService service;
    private final ModelMapper mapper;

    @GetMapping("/cliente/{num}")
    public ResponseEntity<TbPedidosResumenDto> getClienteByNum(@PathVariable String num) {
        return service.findByNum(num)
                .map(this::convertToResumenDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private TbPedidosResumenDto convertToResumenDto(Object entity) {
        return mapper.map(entity, TbPedidosResumenDto.class);
    }
}
