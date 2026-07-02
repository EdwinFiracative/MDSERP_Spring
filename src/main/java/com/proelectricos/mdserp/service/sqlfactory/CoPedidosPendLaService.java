package com.proelectricos.mdserp.service.sqlfactory;

import com.proelectricos.mdserp.model.entity.sqlfactory.CoPedidosPendLa;
import com.proelectricos.mdserp.repository.sqlfactory.CoPedidosPendLaRepository;
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
