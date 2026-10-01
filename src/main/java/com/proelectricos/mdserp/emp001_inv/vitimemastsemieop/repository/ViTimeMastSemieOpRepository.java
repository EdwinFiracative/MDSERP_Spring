package com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.repository;

import com.proelectricos.mdserp.emp001_inv.vitimemastsemieop.ViTimeMastSemieOp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViTimeMastSemieOpRepository extends JpaRepository<ViTimeMastSemieOp, Long> {
}
