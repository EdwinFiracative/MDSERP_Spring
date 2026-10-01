package com.proelectricos.mdserp.emp001_inv.cotuberia.repository;

import com.proelectricos.mdserp.emp001_inv.cotuberia.CoTuberia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoTuberiaRepository extends JpaRepository<CoTuberia, String> {
}


