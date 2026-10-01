package com.proelectricos.mdserp.erpdb.client.repository;

import com.proelectricos.mdserp.erpdb.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
