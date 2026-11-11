package com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.service;

import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.ViMatOpsAbiertas;
import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.repository.ViMatOpsAbiertasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ViMatOpsAbiertasService {

    private final ViMatOpsAbiertasRepository repository;

    /** Materiales de las OPs abiertas del año indicado (por defecto, el año en curso). */
    public List<ViMatOpsAbiertas> findAll(Integer anio) {
        return repository.findMaterialesOpsAbiertas(resolverAnio(anio));
    }

    public List<ViMatOpsAbiertas> findByOp(Integer op, Integer anio) {
        return repository.findMaterialesOpsAbiertasByOp(resolverAnio(anio), op);
    }

    private int resolverAnio(Integer anio) {
        return anio != null ? anio : Year.now().getValue();
    }
}
