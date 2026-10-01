package com.proelectricos.mdserp.emp001_fact.copagos.service;

import com.proelectricos.mdserp.emp001_fact.copagos.CoPagos;
import com.proelectricos.mdserp.emp001_fact.copagos.repository.CoPagosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoPagosService {

    private final CoPagosRepository CoPagosRepository;

    public Iterable<CoPagos> findAllCoPagos() {
        return CoPagosRepository.findAll();
    }
}



