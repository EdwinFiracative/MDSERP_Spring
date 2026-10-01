package com.proelectricos.mdserp.erpdb.referclassification.repository;

import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferClassificationRepository extends JpaRepository<ReferClassification, Long> {
}