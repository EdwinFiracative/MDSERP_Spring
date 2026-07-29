package com.proelectricos.mdserp.service.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.ViOrdAbiertas;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_comp.ViOrdAbiertasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViOrdAbiertasService {

    private final ViOrdAbiertasRepository repository;

    @Transactional(readOnly = true)
    public List<ViOrdAbiertas> findAll() {
        return repository.findAll();
    }
}
