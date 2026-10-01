package com.proelectricos.mdserp.erpdb.thirdparty.repository;

import com.proelectricos.mdserp.erpdb.thirdparty.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
}
