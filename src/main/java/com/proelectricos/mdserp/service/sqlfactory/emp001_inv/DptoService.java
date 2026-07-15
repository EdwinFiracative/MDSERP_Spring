package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Dpto;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.DptoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DptoService {

    private final DptoRepository repository;

    public List<Dpto> findAll() {
        return repository.findAll();
    }
}



