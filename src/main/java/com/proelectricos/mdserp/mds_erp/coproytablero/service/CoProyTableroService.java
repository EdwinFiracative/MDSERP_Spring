package com.proelectricos.mdserp.mds_erp.coproytablero.service;

import com.proelectricos.mdserp.emp004_inv.adic.Adic;
import com.proelectricos.mdserp.mds_erp.coproytablero.CoProyTablero;
import com.proelectricos.mdserp.mds_erp.coproytablero.repository.CoProyTableroRepository;
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



