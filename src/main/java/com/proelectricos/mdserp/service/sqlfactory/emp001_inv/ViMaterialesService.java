package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViMateriales;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViMaterialesRepository;
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


