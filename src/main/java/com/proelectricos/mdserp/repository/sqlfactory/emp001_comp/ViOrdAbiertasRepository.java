package com.proelectricos.mdserp.repository.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.ViOrdAbiertas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViOrdAbiertasRepository extends JpaRepository<ViOrdAbiertas, Long> {
}
