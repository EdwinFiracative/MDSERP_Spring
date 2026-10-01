package com.proelectricos.mdserp.emp001_inv.dpto.service;

import com.proelectricos.mdserp.emp001_inv.dpto.Dpto;
import com.proelectricos.mdserp.emp001_inv.dpto.repository.DptoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DptoService {

    private final DptoRepository repository;

    public List<Dpto> findAll() {
        return repository.findAll();
    }
}



