package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoMp;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoInvNetoMpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoInvNetoMpService {

    private final CoInvNetoMpRepository repository;

    public List<CoInvNetoMp> findAll() {
        return repository.findAll();
    }
}



