package com.proelectricos.mdserp.emp001_fact.pedido.repository;

import com.proelectricos.mdserp.emp001_fact.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>, JpaSpecificationExecutor<Pedido> {

}
