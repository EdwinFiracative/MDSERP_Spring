package com.proelectricos.mdserp.emp001_inv.cocatalogoprod.service;

import com.proelectricos.mdserp.emp001_inv.cocatalogoprod.CoCatalogoProd;
import com.proelectricos.mdserp.emp001_inv.cocatalogoprod.repository.CoCatalogoProdRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoCatalogoProdService {

    private final CoCatalogoProdRepository coCatalogoProdRepository;

    public List<CoCatalogoProd> findAllCoCatalogoProd() {
        return coCatalogoProdRepository.findAll();
    }
}



