package com.proelectricos.mdserp.emp001_inv.vimateriales.repository;

import com.proelectricos.mdserp.emp001_inv.vimateriales.ViMateriales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViMaterialesRepository extends JpaRepository<ViMateriales, Long> {
}


