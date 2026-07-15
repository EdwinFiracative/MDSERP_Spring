package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoEspeciale;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoEspecialeRepository;
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



