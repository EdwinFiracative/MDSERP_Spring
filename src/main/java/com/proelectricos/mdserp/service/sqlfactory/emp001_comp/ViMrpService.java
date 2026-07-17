package com.proelectricos.mdserp.service.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.ViMrp;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_comp.ViMrpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ViMrpService {

    private final ViMrpRepository repository;

    public List<ViMrp> findAll() {
        return repository.findAll();
    }
}
