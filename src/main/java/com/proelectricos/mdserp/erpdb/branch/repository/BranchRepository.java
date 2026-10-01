package com.proelectricos.mdserp.erpdb.branch.repository;

import com.proelectricos.mdserp.erpdb.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
