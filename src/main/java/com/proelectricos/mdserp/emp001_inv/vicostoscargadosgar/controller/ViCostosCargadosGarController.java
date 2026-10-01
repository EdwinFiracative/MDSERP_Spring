package com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.controller;

import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.ViCostosCargadosGar;
import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.dto.ViCostosCargadosGarDto;
import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.service.ViCostosCargadosGarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vi-costos-cargados-gar")
@RequiredArgsConstructor
public class ViCostosCargadosGarController {

    private final ViCostosCargadosGarService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViCostosCargadosGarDto> findAll() {
        return service.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/op/{op}")
    public List<ViCostosCargadosGarDto> findByOP(@PathVariable Integer op) {
        return service.findByOP(op)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Toma el ValorTotal de la OP (el mismo de GET /op/{op}) y lo escribe en el campo
     * UF_CRM_1790282776 del deal de Bitrix24 indicado, usando crm.deal.update.
     *
     * Ejemplo: POST /api/vi-costos-cargados-gar/op/1234/bitrix/5678
     * Respuesta: {"op":1234,"dealId":5678,"campo":"UF_CRM_1790282776","valorTotal":11778708,"bitrix":{"result":true,...}}
     */
    @PostMapping("/op/{op}/bitrix/{dealId}")
    public Map<String, Object> enviarValorTotalABitrix(@PathVariable Integer op, @PathVariable Long dealId) {
        return service.enviarValorTotalABitrix(op, dealId);
    }

    private ViCostosCargadosGarDto convertToDto(ViCostosCargadosGar entity) {
        return mapper.map(entity, ViCostosCargadosGarDto.class);
    }
}
