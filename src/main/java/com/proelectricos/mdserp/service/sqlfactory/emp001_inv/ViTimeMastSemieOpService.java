package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViTimeMastSemieOp;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViTimeMastSemieOpRepository;
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
