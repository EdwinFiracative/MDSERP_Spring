package com.proelectricos.mdserp.service.sqlfactory;

import com.proelectricos.mdserp.model.entity.sqlfactory.CoCarteraNegocio;
import com.proelectricos.mdserp.repository.sqlfactory.CoCarteraNegocioRepository;
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
