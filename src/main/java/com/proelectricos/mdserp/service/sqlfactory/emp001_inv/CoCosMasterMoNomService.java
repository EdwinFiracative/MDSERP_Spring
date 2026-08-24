package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoCosMasterMoNom;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoCosMasterMoNomRepository;
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
