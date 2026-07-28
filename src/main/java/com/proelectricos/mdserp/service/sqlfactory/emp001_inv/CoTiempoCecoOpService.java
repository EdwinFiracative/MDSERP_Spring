package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoTiempoCecoOp;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoTiempoCecoOpRepository;
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
