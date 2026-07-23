package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.PedidoRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.PedidoSpecifications;
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
