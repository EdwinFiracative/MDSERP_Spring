package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoFacturacion;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoFacturacionRepository;
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



