package com.proelectricos.mdserp.emp001_fact.cocarteranegocio.service;

import com.proelectricos.mdserp.emp001_fact.cocarteranegocio.CoCarteraNegocio;
import com.proelectricos.mdserp.emp001_fact.cocarteranegocio.repository.CoCarteraNegocioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoCarteraNegocioService {

    private final CoCarteraNegocioRepository coCarteraNegocioRepository;

    public List<CoCarteraNegocio> findAll() {
        return coCarteraNegocioRepository.findAll();
    }
}



