package com.proelectricos.mdserp.emp001_inv.viopmastertimececo.repository;

import com.proelectricos.mdserp.emp001_inv.viopmastertimececo.ViOpMasterTimeCeco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViOpMasterTimeCecoRepository extends JpaRepository<ViOpMasterTimeCeco, Long> {
}
