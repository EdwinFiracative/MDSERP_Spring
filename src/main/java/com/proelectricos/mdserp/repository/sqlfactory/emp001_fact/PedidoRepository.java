package com.proelectricos.mdserp.repository.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>, JpaSpecificationExecutor<Pedido> {

}
