package com.proelectricos.mdserp.repository.sqlfactory.emp001_comp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp.ViMrp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViMrpRepository extends JpaRepository<ViMrp, String> {
}
