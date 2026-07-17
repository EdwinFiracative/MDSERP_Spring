package com.proelectricos.mdserp.repository.sqlfactory.emp001_ofer;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_ofer.VwAvioferpedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwAvioferpedidoRepository extends JpaRepository<VwAvioferpedido, Integer> {
    List<VwAvioferpedido> findByNUM(String NUM);
}
