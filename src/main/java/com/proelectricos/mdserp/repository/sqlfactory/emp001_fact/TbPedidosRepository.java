package com.proelectricos.mdserp.repository.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.TbPedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TbPedidosRepository extends JpaRepository<TbPedidos, Integer> {
    Optional<TbPedidos> findFirstByNum(String num);
}
