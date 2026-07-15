package com.proelectricos.mdserp.service.sqlfactory.emp004_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv.Adic;
import com.proelectricos.mdserp.repository.sqlfactory.emp004_inv.AdicRepository;
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



