package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoOpProceso;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoOpProcesoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoOpProcesoService {

    private final CoOpProcesoRepository repository;

    public List<CoOpProceso> findAll() {
        return repository.findAll();
    }
}



