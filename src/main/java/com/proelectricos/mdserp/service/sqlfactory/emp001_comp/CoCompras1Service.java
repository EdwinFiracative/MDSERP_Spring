package com.proelectricos.mdserp.service.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.CoCompras1;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_comp.CoCompras1Repository;
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



