package com.proelectricos.mdserp.emp001_inv.vimaterialesop.service;

import com.proelectricos.mdserp.emp001_inv.vimaterialesop.ViMaterialesOP;
import com.proelectricos.mdserp.emp001_inv.vimaterialesop.repository.ViMaterialesOPRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ViMaterialesOPService {

    private final ViMaterialesOPRepository repository;

    public List<ViMaterialesOP> findAll() {
        return repository.findAll();
    }

    public List<ViMaterialesOP> findByOP(Integer OP) {
        return repository.findByOP(OP);
    }
}


