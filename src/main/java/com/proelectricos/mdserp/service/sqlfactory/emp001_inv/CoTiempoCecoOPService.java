package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoTiempoCecoOP;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoTiempoCecoOPRepository;
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
