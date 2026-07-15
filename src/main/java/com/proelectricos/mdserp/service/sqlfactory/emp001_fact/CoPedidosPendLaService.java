package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPedidosPendLa;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoPedidosPendLaRepository;
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



