package com.proelectricos.mdserp.emp001_fact.viewpedidocuentacobro.dto;

import com.proelectricos.mdserp.emp001_fact.cliente.dto.ClienteDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader}
 */
@Getter
@Setter
public class ViewPedidoCuentaCobro implements Serializable {

    @Size(max = 7)
    String num;
    @Size(max = 15)
    ClienteDto cliente;
}