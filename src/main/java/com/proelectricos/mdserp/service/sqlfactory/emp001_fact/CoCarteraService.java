package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoCartera;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.CoCarteraRepository;
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



