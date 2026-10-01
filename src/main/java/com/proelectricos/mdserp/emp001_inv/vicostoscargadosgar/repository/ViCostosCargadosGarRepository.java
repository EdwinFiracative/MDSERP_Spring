package com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.repository;

import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.ViCostosCargadosGar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViCostosCargadosGarRepository extends JpaRepository<ViCostosCargadosGar, Long> {
    List<ViCostosCargadosGar> findByOp(Integer op);
}
