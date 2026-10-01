package com.proelectricos.mdserp.emp001_inv.coespeciale.service;

import com.proelectricos.mdserp.emp001_inv.coespeciale.CoEspeciale;
import com.proelectricos.mdserp.emp001_inv.coespeciale.repository.CoEspecialeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoEspecialeService {

    private final CoEspecialeRepository coEspecialeRepository;

    public List<CoEspeciale> findAllCoEspeciales() {
        return coEspecialeRepository.findAll();
    }
}



