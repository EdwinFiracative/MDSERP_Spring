package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMaterialesOP;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViMaterialesOPRepository;
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
}



