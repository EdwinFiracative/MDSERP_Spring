package com.proelectricos.mdserp.emp001_fact.copedidospendla.service;

import com.proelectricos.mdserp.emp001_fact.copedidospendla.CoPedidosPendLa;
import com.proelectricos.mdserp.emp001_fact.copedidospendla.repository.CoPedidosPendLaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoPedidosPendLaService {

    private final CoPedidosPendLaRepository repository;

    public List<CoPedidosPendLa> findAll() {
        return repository.findAll();
    }
}



