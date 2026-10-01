package com.proelectricos.mdserp.emp001_fact.tbpedidos.repository;

import com.proelectricos.mdserp.emp001_fact.tbpedidos.TbPedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TbPedidosRepository extends JpaRepository<TbPedidos, Integer> {
    Optional<TbPedidos> findFirstByNum(String num);
}
