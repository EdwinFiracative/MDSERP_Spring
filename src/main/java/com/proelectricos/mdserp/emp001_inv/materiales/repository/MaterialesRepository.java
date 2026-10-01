package com.proelectricos.mdserp.emp001_inv.materiales.repository;

import com.proelectricos.mdserp.emp001_inv.materiales.Materiales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialesRepository extends JpaRepository<Materiales, Long> {
}


