package com.proelectricos.mdserp.emp001_fact.viewpedidocuentacobro.service;

import com.proelectricos.mdserp.emp001_fact.pedido.dto.PedidoFilterRequest;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.repository.ViewErpPedidoHeaderSpecifications;
import com.proelectricos.mdserp.emp001_fact.viewpedidocuentacobro.repository.ViewPedidoCuentaCobroRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ViewPedidoCuentaCobroService {

    private final ViewPedidoCuentaCobroRepository repository;

    public List<ViewErpPedidoHeader> findAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public List<ViewErpPedidoHeader> findAll(Pageable pageable, PedidoFilterRequest filter) {
        return repository.findAll(ViewErpPedidoHeaderSpecifications.withFilter(filter), pageable).getContent();
    }

    public Optional<ViewErpPedidoHeader> findById(String num) {
        return repository.findById(num);
    }
}
