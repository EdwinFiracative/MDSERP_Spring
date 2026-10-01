package com.proelectricos.mdserp.emp001_inv.cotiempocecoop.repository;

import com.proelectricos.mdserp.emp001_inv.cotiempocecoop.CoTiempoCecoOp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoTiempoCecoOpRepository extends JpaRepository<CoTiempoCecoOp, Long> {
}
