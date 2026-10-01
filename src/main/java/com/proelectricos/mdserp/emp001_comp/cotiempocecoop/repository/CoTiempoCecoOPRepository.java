package com.proelectricos.mdserp.emp001_comp.cotiempocecoop.repository;

import com.proelectricos.mdserp.emp001_comp.cotiempocecoop.CoTiempoCecoOP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoTiempoCecoOPRepository extends JpaRepository<CoTiempoCecoOP, Long> {
}
