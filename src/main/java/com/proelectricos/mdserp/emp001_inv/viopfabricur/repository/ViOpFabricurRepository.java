package com.proelectricos.mdserp.emp001_inv.viopfabricur.repository;

import com.proelectricos.mdserp.emp001_inv.viopfabricur.ViOpFabricur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViOpFabricurRepository extends JpaRepository<ViOpFabricur, Integer> {
}


