package com.proelectricos.mdserp.emp001_inv.vwpedidosclasificado.repository;

import com.proelectricos.mdserp.emp001_inv.vwpedidosclasificado.VwPedidosClasificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VwPedidosClasificadoRepository extends JpaRepository<VwPedidosClasificado, Long> {
}
