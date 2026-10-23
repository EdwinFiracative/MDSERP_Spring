package com.proelectricos.mdserp.emp001_fact.cliente.repository;

import com.proelectricos.mdserp.emp001_fact.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    interface ClienteZona {
        String getCod();
        String getNit();
        String getZona();
    }

    @Query("select c.cod as cod, c.nit as nit, c.zona as zona from Cliente c where c.zona is not null and trim(c.zona) <> ''")
    List<ClienteZona> findAllWithZona();
}
