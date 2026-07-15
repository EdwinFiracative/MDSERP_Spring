package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoCarteraNegocio;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoCarteraNegocioRepository;
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



