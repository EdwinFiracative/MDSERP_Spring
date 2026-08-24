package com.proelectricos.mdserp.repository.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViTimeMastSemieOp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViTimeMastSemieOpRepository extends JpaRepository<ViTimeMastSemieOp, Long> {
}
