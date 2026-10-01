package com.proelectricos.mdserp.emp001_fact.copagnegocio.service;

import com.proelectricos.mdserp.emp001_fact.copagnegocio.CoPagNegocio;
import com.proelectricos.mdserp.emp001_fact.copagnegocio.repository.CoPagNegocioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoPagNegocioService {

    private final CoPagNegocioRepository coPagNegocioRepository;

    public Iterable<CoPagNegocio> findAllCoPagNegocio() {
        return coPagNegocioRepository.findAll();
    }
}



