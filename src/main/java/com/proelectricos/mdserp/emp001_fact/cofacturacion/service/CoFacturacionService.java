package com.proelectricos.mdserp.emp001_fact.cofacturacion.service;

import com.proelectricos.mdserp.emp001_fact.cofacturacion.CoFacturacion;
import com.proelectricos.mdserp.emp001_fact.cofacturacion.repository.CoFacturacionRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoFacturacionService {

    private final CoFacturacionRepository CoFacturacionRepository;

    public Iterable<CoFacturacion> findAllCoFacturacion() {
        return CoFacturacionRepository.findAll();
    }
}



