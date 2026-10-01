package com.proelectricos.mdserp.emp001_inv.viopfabricur.service;

import com.proelectricos.mdserp.emp001_inv.viopfabricur.ViOpFabricur;
import com.proelectricos.mdserp.emp001_inv.viopfabricur.repository.ViOpFabricurRepository;
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



