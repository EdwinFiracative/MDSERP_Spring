package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PedidoService {
    private final PedidoRepository PedidoRepository;

    public List<Pedido> findAllPedidos() {
        return PedidoRepository.findAll(PageRequest.of(1, 1000)).getContent();
    }

}
