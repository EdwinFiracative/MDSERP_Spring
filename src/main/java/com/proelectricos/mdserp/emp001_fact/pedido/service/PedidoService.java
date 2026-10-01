package com.proelectricos.mdserp.emp001_fact.pedido.service;

import com.proelectricos.mdserp.emp001_fact.pedido.Pedido;
import com.proelectricos.mdserp.emp001_fact.pedido.dto.PedidoFilterRequest;
import com.proelectricos.mdserp.emp001_fact.pedido.repository.PedidoRepository;
import com.proelectricos.mdserp.emp001_fact.pedido.repository.PedidoSpecifications;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PedidoService {
    private final PedidoRepository PedidoRepository;

    public List<Pedido> findAllPedidos(Pageable pageable) {
        return PedidoRepository.findAll(pageable).getContent();
    }

    public List<Pedido> findAllPedidos(Pageable pageable, PedidoFilterRequest filter) {
        Page<Pedido> pedidos = PedidoRepository.findAll(PedidoSpecifications.withFilter(filter), pageable);
        return pedidos.getContent();
    }

}
