package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.ViewErpPedidoHeaderRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.ViewErpPedidoHeaderSpecifications;
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
