package com.proelectricos.mdserp.emp001_comp.vimrp.repository;

import com.proelectricos.mdserp.emp001_comp.vimrp.ViMrp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViMrpRepository extends JpaRepository<ViMrp, String> {
}
