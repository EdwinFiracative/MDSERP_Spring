package com.proelectricos.mdserp.controller.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.ViewPedidoCuentaCobro;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader;
import com.proelectricos.mdserp.service.sqlfactory.emp001_fact.ViewPedidoCuentaCobroService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/view-pedido-cuenta-cobro")
public class ViewPedidoCuentaCobroController {

    private final ViewPedidoCuentaCobroService service;
    private final ModelMapper mapper;

    @GetMapping
    public List<ViewPedidoCuentaCobro> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "num,asc") String sort
    ) {
        PageRequest pageRequest = createPageRequest(page, size, sort);
        return service.findAll(pageRequest)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/filter")
    public List<ViewPedidoCuentaCobro> getFiltered(
            @RequestBody(required = false) PedidoFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "num,asc") String sort
    ) {
        PageRequest pageRequest = createPageRequest(page, size, sort);
        return service.findAll(pageRequest, filter)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{num}")
    public ResponseEntity<ViewPedidoCuentaCobro> getById(@PathVariable String num) {
        return service.findById(num)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private ViewPedidoCuentaCobro convertToDto(ViewErpPedidoHeader entity) {
        return mapper.map(entity, ViewPedidoCuentaCobro.class);
    }

    private PageRequest createPageRequest(int page, int size, String sort) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = sortParts.length > 1
                ? Sort.Direction.fromOptionalString(sortParts[1].trim()).orElse(Sort.Direction.ASC)
                : Sort.Direction.ASC;

        int cappedSize = Math.min(Math.max(size, 1), 500);
        return PageRequest.of(Math.max(page, 0), cappedSize, Sort.by(direction, sortField));
    }
}
