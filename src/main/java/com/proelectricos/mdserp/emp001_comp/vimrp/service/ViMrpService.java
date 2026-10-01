package com.proelectricos.mdserp.emp001_comp.vimrp.service;

import com.proelectricos.mdserp.emp001_comp.vimrp.ViMrp;
import com.proelectricos.mdserp.emp001_comp.vimrp.repository.ViMrpRepository;
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
