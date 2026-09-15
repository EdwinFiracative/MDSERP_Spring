package com.proelectricos.mdserp.repository.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViCostosCargadosGar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViCostosCargadosGarRepository extends JpaRepository<ViCostosCargadosGar, Long> {
    List<ViCostosCargadosGar> findByOp(Integer op);
}
