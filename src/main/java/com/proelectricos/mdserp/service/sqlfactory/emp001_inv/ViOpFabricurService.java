package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViOpFabricur;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.ViOpFabricurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ViOpFabricurService {

    private final ViOpFabricurRepository viOpFabricurRepository;

    public List<ViOpFabricur> findAllViOpFabricur() {
        return viOpFabricurRepository.findAll();
    }
}



