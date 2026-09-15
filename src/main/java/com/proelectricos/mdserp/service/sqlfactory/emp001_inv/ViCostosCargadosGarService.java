package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViCostosCargadosGar;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViCostosCargadosGarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViCostosCargadosGarService {

    private final ViCostosCargadosGarRepository repository;

    @Transactional(readOnly = true)
    public List<ViCostosCargadosGar> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ViCostosCargadosGar> findByOP(Integer op) {
        return repository.findByOp(op);
    }
}
