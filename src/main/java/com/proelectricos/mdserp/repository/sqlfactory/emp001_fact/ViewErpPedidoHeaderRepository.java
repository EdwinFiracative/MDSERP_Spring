package com.proelectricos.mdserp.repository.sqlfactory.emp001_fact;


import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ViewErpPedidoHeaderRepository extends JpaRepository<ViewErpPedidoHeader, String>, JpaSpecificationExecutor<ViewErpPedidoHeader> {
}