package com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.service;

import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.ViTimeMastSemieOp;
import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.repository.ViTimeMastSemieOpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViTimeMastSemieOpService {

    private final ViTimeMastSemieOpRepository repository;

    @Transactional(readOnly = true)
    public List<ViTimeMastSemieOp> findAll() {
        return repository.findAll();
    }
}
