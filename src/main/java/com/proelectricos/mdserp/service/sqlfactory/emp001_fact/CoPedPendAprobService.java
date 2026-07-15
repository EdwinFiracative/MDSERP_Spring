package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPedPendAprob;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoPedPendAprobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoPedPendAprobService {

    private final CoPedPendAprobRepository repository;

    public List<CoPedPendAprob> findAll() {
        return repository.findAll();
    }
}



