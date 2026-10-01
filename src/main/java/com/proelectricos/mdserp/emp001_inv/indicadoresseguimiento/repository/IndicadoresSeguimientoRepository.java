package com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.repository;

import com.proelectricos.mdserp.emp001_inv.indicadoresseguimiento.IndicadoresSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicadoresSeguimientoRepository extends JpaRepository<IndicadoresSeguimiento, Long> {
}
