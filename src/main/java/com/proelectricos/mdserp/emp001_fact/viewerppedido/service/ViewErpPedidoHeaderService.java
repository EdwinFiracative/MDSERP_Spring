package com.proelectricos.mdserp.emp001_fact.viewerppedido.service;

import com.proelectricos.mdserp.emp001_fact.pedido.dto.PedidoFilterRequest;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.repository.ViewErpPedidoHeaderRepository;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.repository.ViewErpPedidoHeaderSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ViewErpPedidoHeaderService {
    private final ViewErpPedidoHeaderRepository ViewErpPedidoHeaderRepository;

    public List<ViewErpPedidoHeader> findAllPedidos(Pageable pageable) {
        return ViewErpPedidoHeaderRepository.findAll(pageable).getContent();
    }

    public List<ViewErpPedidoHeader> findAllPedidos(Pageable pageable, PedidoFilterRequest filter) {
        Page<ViewErpPedidoHeader> pedidos = ViewErpPedidoHeaderRepository.findAll(ViewErpPedidoHeaderSpecifications.withFilter(filter), pageable);
        return pedidos.getContent();
    }
}
