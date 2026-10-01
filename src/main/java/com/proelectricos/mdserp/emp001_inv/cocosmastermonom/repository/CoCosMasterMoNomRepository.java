package com.proelectricos.mdserp.emp001_inv.cocosmastermonom.repository;

import com.proelectricos.mdserp.emp001_inv.cocosmastermonom.CoCosMasterMoNom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoCosMasterMoNomRepository extends JpaRepository<CoCosMasterMoNom, String> {
}
