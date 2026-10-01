package com.proelectricos.mdserp.emp001_comp.viordabiertas.service;

import com.proelectricos.mdserp.emp001_comp.viordabiertas.ViOrdAbiertas;
import com.proelectricos.mdserp.emp001_comp.viordabiertas.repository.ViOrdAbiertasRepository;
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
