package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader}
 */
@Getter
@Setter
public class ViewPedidoCuentaCobro implements Serializable {

    @Size(max = 7)
    String num;
    @Size(max = 15)
    ClienteDto cliente;
}