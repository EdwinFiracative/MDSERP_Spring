package com.proelectricos.mdserp.emp001_ofer.vwavioferpedido.controller;

import com.proelectricos.mdserp.emp001_ofer.vwavioferpedido.dto.VwAvioferpedidoDto;
import com.proelectricos.mdserp.emp001_ofer.vwavioferpedido.service.VwAvioferpedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vw-avioferpedido")
@RequiredArgsConstructor
public class VwAvioferpedidoController {

    private final VwAvioferpedidoService service;

    @GetMapping
    public List<VwAvioferpedidoDto> findAll(@RequestParam(name = "NUM", required = false) String NUM) {
        if (NUM == null || NUM.isBlank()) {
            return service.findAll();
        }
        return service.findByNUM(NUM.trim());
    }
}
