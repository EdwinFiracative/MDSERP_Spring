package com.proelectricos.mdserp.emp001_inv.vimaterialesop.repository;

import com.proelectricos.mdserp.emp001_inv.vimaterialesop.ViMaterialesOP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViMaterialesOPRepository extends JpaRepository<ViMaterialesOP, Long> {
    List<ViMaterialesOP> findByOP(Integer OP);
}

