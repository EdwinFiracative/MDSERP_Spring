package com.proelectricos.mdserp.repository.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoTiempoCecoOp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoTiempoCecoOpRepository extends JpaRepository<CoTiempoCecoOp, Long> {
}
