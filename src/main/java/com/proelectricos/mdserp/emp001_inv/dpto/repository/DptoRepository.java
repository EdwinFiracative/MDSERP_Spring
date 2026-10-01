package com.proelectricos.mdserp.emp001_inv.dpto.repository;

import com.proelectricos.mdserp.emp001_inv.dpto.Dpto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DptoRepository extends JpaRepository<Dpto, Integer> {
}


