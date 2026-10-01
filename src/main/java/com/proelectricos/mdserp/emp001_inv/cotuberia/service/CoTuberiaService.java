package com.proelectricos.mdserp.emp001_inv.cotuberia.service;

import com.proelectricos.mdserp.emp001_inv.cotuberia.CoTuberia;
import com.proelectricos.mdserp.emp001_inv.cotuberia.repository.CoTuberiaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoTuberiaService {

    private final CoTuberiaRepository coTuberiaRepository;

    public List<CoTuberia> findAllCoTuberia() {
        return coTuberiaRepository.findAll();
    }
}



