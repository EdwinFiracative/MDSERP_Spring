package com.proelectricos.mdserp.erpdb.project.repository;

import com.proelectricos.mdserp.erpdb.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
