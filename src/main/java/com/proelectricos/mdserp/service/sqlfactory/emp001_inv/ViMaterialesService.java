package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMateriales;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViMaterialesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ViMaterialesService {

    private final ViMaterialesRepository repository;

    public List<ViMateriales> findAll() {
        return repository.findAll();
    }
}



