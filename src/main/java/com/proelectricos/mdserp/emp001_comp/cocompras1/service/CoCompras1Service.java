package com.proelectricos.mdserp.emp001_comp.cocompras1.service;

import com.proelectricos.mdserp.emp001_comp.cocompras1.CoCompras1;
import com.proelectricos.mdserp.emp001_comp.cocompras1.repository.CoCompras1Repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoCompras1Service {

    private final CoCompras1Repository repository;

    public List<CoCompras1> findAll() {
        return repository.findAll();
    }
}



