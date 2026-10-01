package com.proelectricos.mdserp.emp001_fact.copagoscar.service;

import com.proelectricos.mdserp.emp001_fact.copagoscar.CoPagosCar;
import com.proelectricos.mdserp.emp001_fact.copagoscar.repository.CoPagosCarRepository;
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



