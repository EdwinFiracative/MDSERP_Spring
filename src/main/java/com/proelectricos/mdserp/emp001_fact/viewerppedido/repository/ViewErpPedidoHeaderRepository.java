package com.proelectricos.mdserp.emp001_fact.viewerppedido.repository;

import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ViewErpPedidoHeaderRepository extends JpaRepository<ViewErpPedidoHeader, String>, JpaSpecificationExecutor<ViewErpPedidoHeader> {
}