package com.proelectricos.mdserp.emp001_fact.viewpedidocuentacobro.repository;

import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewPedidoCuentaCobroRepository extends JpaRepository<ViewErpPedidoHeader, String>, JpaSpecificationExecutor<ViewErpPedidoHeader> {
}
