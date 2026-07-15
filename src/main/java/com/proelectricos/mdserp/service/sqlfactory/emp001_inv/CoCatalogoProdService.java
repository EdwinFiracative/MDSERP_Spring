package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoCatalogoProd;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoCatalogoProdRepository;
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



