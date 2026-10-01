package com.proelectricos.mdserp.emp001_inv.materiales.service;

import com.proelectricos.mdserp.emp001_inv.materiales.Materiales;
import com.proelectricos.mdserp.emp001_inv.materiales.repository.MaterialesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MaterialesService {

    private final MaterialesRepository repository;

    public List<Materiales> findAll() {
        return repository.findAll();
    }
}



