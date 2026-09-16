package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.IndicadoresSeguimiento;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.IndicadoresSeguimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicadoresSeguimientoService {

    private final IndicadoresSeguimientoRepository repository;

    @Transactional(readOnly = true)
    public List<IndicadoresSeguimiento> findAll() {
        return repository.findAll();
    }
}
