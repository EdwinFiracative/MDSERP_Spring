package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.VwPedidosClasificado;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.VwPedidosClasificadoRepository;
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
