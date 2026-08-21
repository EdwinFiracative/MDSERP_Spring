package com.proelectricos.mdserp.repository.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferClassificationRepository extends JpaRepository<ReferClassification, Long> {
}