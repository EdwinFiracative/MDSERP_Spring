package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Referencia}
 */
@Getter
@Setter
public class ReferenciaBasicDto implements Serializable {
    @Size(max = 20)
    String cod;
    BigDecimal exist;
    BigDecimal cstd;
}