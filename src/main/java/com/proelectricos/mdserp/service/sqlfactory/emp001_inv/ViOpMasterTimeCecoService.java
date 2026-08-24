package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViOpMasterTimeCeco;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViOpMasterTimeCecoRepository;
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
