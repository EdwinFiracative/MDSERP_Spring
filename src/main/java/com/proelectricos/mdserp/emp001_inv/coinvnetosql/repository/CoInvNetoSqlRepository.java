package com.proelectricos.mdserp.emp001_inv.coinvnetosql.repository;

import com.proelectricos.mdserp.emp001_inv.coinvnetosql.CoInvNetoSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoInvNetoSqlRepository extends JpaRepository<CoInvNetoSql, Long> {
}


