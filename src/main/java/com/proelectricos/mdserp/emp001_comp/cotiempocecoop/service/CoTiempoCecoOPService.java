package com.proelectricos.mdserp.emp001_comp.cotiempocecoop.service;

import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.CoTiempoCecoOP;
import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.repository.CoTiempoCecoOPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoTiempoCecoOPService {

    private final CoTiempoCecoOPRepository repository;

    @Transactional(readOnly = true)
    public List<CoTiempoCecoOP> findAll() {
        return repository.findAll();
    }
}
