package com.proelectricos.mdserp.emp001_inv.vwpedidosclasificado.service;

import com.proelectricos.mdserp.emp001_inv.vwpedidosclasificado.VwPedidosClasificado;
import com.proelectricos.mdserp.emp001_inv.vwpedidosclasificado.repository.VwPedidosClasificadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VwPedidosClasificadoService {

    private final VwPedidosClasificadoRepository repository;

    @Transactional(readOnly = true)
    public List<VwPedidosClasificado> findAll() {
        return repository.findAll();
    }
}
