package com.proelectricos.mdserp.emp001_comp.viordabiertas.repository;

import com.proelectricos.mdserp.emp001_comp.viordabiertas.ViOrdAbiertas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViOrdAbiertasRepository extends JpaRepository<ViOrdAbiertas, Long> {
}
