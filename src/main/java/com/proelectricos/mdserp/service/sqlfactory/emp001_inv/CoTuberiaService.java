package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoTuberia;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoTuberiaRepository;
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



