package com.proelectricos.mdserp.emp001_inv.cocatalogoprod.repository;

import com.proelectricos.mdserp.emp001_inv.cocatalogoprod.CoCatalogoProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoCatalogoProdRepository extends JpaRepository<CoCatalogoProd, String> {
}


