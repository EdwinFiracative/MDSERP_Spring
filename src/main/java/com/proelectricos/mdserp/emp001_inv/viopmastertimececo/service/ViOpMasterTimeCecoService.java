package com.proelectricos.mdserp.emp001_inv.viopmastertimececo.service;

import com.proelectricos.mdserp.emp001_inv.viopmastertimececo.ViOpMasterTimeCeco;
import com.proelectricos.mdserp.emp001_inv.viopmastertimececo.repository.ViOpMasterTimeCecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViOpMasterTimeCecoService {

    private final ViOpMasterTimeCecoRepository repository;

    @Transactional(readOnly = true)
    public List<ViOpMasterTimeCeco> findAll() {
        return repository.findAll();
    }
}
