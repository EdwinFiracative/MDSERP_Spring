package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagNegocio;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoPagNegocioRepository;
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



