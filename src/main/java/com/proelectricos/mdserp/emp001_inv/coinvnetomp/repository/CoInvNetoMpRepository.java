package com.proelectricos.mdserp.emp001_inv.coinvnetomp.repository;

import com.proelectricos.mdserp.emp001_inv.coinvnetomp.CoInvNetoMp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoInvNetoMpRepository extends JpaRepository<CoInvNetoMp, String> {
}
