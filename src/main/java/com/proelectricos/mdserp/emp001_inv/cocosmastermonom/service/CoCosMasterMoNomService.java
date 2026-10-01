package com.proelectricos.mdserp.emp001_inv.cocosmastermonom.service;

import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.CoCosMasterMoNom;
import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.repository.CoCosMasterMoNomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoCosMasterMoNomService {

    private final CoCosMasterMoNomRepository repository;

    @Transactional(readOnly = true)
    public List<CoCosMasterMoNom> findAll() {
        return repository.findAll();
    }
}
