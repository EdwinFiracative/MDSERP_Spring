package com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.service;

import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.IndicadoresSeguimiento;
import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.repository.IndicadoresSeguimientoRepository;
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
