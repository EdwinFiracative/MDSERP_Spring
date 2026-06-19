package com.proelectricos.mdserp.service.sqlfactory;

import com.proelectricos.mdserp.model.entity.sqlfactory.CoCartera;
import com.proelectricos.mdserp.repository.sqlfactory.CoCarteraRepository;
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
