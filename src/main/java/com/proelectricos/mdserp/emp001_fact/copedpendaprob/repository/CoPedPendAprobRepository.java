package com.proelectricos.mdserp.emp001_fact.copedpendaprob.repository;

import com.proelectricos.mdserp.emp001_fact.copedpendaprob.CoPedPendAprob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoPedPendAprobRepository extends JpaRepository<CoPedPendAprob, Long> {
}


