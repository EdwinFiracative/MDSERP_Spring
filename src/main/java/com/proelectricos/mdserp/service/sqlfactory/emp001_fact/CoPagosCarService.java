package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagosCar;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoPagosCarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoPagosCarService {

    private final CoPagosCarRepository repository;

    public List<CoPagosCar> findAll() {
        return repository.findAll();
    }
}



