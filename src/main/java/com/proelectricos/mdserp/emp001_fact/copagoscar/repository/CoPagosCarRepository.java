package com.proelectricos.mdserp.emp001_fact.copagoscar.repository;

import com.proelectricos.mdserp.emp001_fact.copagoscar.CoPagosCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoPagosCarRepository extends JpaRepository<CoPagosCar, Long> {
}


