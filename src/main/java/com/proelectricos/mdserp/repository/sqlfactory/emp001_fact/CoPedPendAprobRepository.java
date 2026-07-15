package com.proelectricos.mdserp.repository.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPedPendAprob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoPedPendAprobRepository extends JpaRepository<CoPedPendAprob, Long> {
}


