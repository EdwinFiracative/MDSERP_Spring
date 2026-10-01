package com.proelectricos.mdserp.emp001_fact.copedpendaprob.service;

import com.proelectricos.mdserp.emp001_fact.copedpendaprob.CoPedPendAprob;
import com.proelectricos.mdserp.emp001_fact.copedpendaprob.repository.CoPedPendAprobRepository;
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



