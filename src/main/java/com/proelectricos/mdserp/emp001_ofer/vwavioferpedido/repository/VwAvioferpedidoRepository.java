package com.proelectricos.mdserp.emp001_ofer.vwavioferpedido.repository;

import com.proelectricos.mdserp.emp001_ofer.vwavioferpedido.VwAvioferpedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwAvioferpedidoRepository extends JpaRepository<VwAvioferpedido, Integer> {
    List<VwAvioferpedido> findByNUM(String NUM);
}
