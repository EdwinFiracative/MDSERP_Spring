package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Materiales;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.MaterialesRepository;
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



