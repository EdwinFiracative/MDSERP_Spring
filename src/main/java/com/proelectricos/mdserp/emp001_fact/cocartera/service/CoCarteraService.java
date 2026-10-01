package com.proelectricos.mdserp.emp001_fact.cocartera.service;

import com.proelectricos.mdserp.emp001_fact.cocartera.CoCartera;
import com.proelectricos.mdserp.emp001_fact.cocartera.repository.CoCarteraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoCarteraService {

    private final CoCarteraRepository coCarteraRepository;

    public List<CoCartera> findAll() {
        return coCarteraRepository.findAll();
    }
}



