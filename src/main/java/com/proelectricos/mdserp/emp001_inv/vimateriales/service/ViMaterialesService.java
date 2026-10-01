package com.proelectricos.mdserp.emp001_inv.vimateriales.service;

import com.proelectricos.mdserp.emp001_inv.vimateriales.ViMateriales;
import com.proelectricos.mdserp.emp001_inv.vimateriales.repository.ViMaterialesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ViMaterialesService {

    private final ViMaterialesRepository repository;

    public Page<ViMateriales> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}


