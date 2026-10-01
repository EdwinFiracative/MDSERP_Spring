package com.proelectricos.mdserp.emp001_inv.coopproceso.service;

import com.proelectricos.mdserp.emp001_inv.coopproceso.CoOpProceso;
import com.proelectricos.mdserp.emp001_inv.coopproceso.repository.CoOpProcesoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoOpProcesoService {

    private final CoOpProcesoRepository repository;

    public List<CoOpProceso> findAll() {
        return repository.findAll();
    }
}



