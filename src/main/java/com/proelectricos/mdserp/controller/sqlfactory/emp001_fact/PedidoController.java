package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.ViewErpPedidoHeaderDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.ViewErpPedidoReferenceDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.PedidoService;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.ViewErpPedidoHeaderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final ViewErpPedidoHeaderService ViewErpPedidoHeaderService;


    @GetMapping
    public List<PedidoDto> getPedido(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = sortParts.length > 1
                ? Sort.Direction.fromOptionalString(sortParts[1].trim()).orElse(Sort.Direction.ASC)
                : Sort.Direction.ASC;

        int cappedSize = Math.min(Math.max(size, 1), 500);
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), cappedSize, Sort.by(direction, sortField));

        return PedidoService.findAllPedidos(pageRequest)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/filter")
    public List<PedidoDto> getPedidoFiltered(
            @RequestBody(required = false) PedidoFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size,


            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = sortParts.length > 1
                ? Sort.Direction.fromOptionalString(sortParts[1].trim()).orElse(Sort.Direction.ASC)
                : Sort.Direction.ASC;


        int cappedSize = Math.min(Math.max(size, 1), 55500);

        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), cappedSize, Sort.by(direction, sortField));

        return PedidoService.findAllPedidos(pageRequest, filter)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/filter2")
    public List<ViewErpPedidoHeaderDto> getPedidoFiltered2(
            @RequestBody(required = false) PedidoFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size,


            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = sortParts.length > 1
                ? Sort.Direction.fromOptionalString(sortParts[1].trim()).orElse(Sort.Direction.ASC)
                : Sort.Direction.ASC;


        int cappedSize = Math.min(Math.max(size, 1), 55500);

        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), cappedSize, Sort.by(direction, sortField));

        return ViewErpPedidoHeaderService.findAllPedidos(pageRequest, filter)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PedidoDto convertToDto(Pedido entity) {
        return mapper.map(entity, PedidoDto.class);
    }

    private ViewErpPedidoHeaderDto convertToDto(ViewErpPedidoHeader entity) {
        return mapper.map(entity, ViewErpPedidoHeaderDto.class);
    }

}
