package com.proelectricos.mdserp.erpdb.reference.repository;

import com.proelectricos.mdserp.erpdb.reference.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
}
