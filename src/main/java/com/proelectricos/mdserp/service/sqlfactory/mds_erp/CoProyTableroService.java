package com.proelectricos.mdserp.service.sqlfactory.mds_erp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv.Adic;
import com.proelectricos.mdserp.model.entity.sqlfactory.mds_erp.CoProyTablero;
import com.proelectricos.mdserp.repository.sqlfactory.mds_erp.CoProyTableroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoProyTableroService {
    private final CoProyTableroRepository CoProyTableroRepository;

    public Iterable<CoProyTablero> findAllCoProyTablero() {
        return CoProyTableroRepository.findAll();
    }
}



