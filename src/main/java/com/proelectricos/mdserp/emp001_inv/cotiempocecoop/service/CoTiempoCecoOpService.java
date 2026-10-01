package com.proelectricos.mdserp.emp001_inv.cotiempocecoop.service;

import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.CoTiempoCecoOp;
import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.repository.CoTiempoCecoOpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoTiempoCecoOpService {

    private final CoTiempoCecoOpRepository repository;

    @Transactional(readOnly = true)
    public List<CoTiempoCecoOp> findAll() {
        return repository.findAll();
    }
}
