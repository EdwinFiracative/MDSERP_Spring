package com.proelectricos.mdserp.emp001_inv.referencia.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.emp001_inv.referencia.Referencia}
 */
@Getter
@Setter
public class ReferenciaBasicDto implements Serializable {
    @Size(max = 20)
    String cod;
    BigDecimal exist;
    BigDecimal cstd;
}