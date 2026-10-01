package com.proelectricos.mdserp.emp004_inv.adic.service;

import com.proelectricos.mdserp.emp004_inv.adic.Adic;
import com.proelectricos.mdserp.emp004_inv.adic.repository.AdicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdicService {

    private final AdicRepository adicRepository;

  public Iterable<Adic> findAllAdics() {
        return adicRepository.findAll();
    }

}



