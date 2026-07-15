package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagos;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoPagosRepository;
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



