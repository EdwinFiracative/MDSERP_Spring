package com.proelectricos.mdserp.model.dto.sqlfactory.emp004_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv.Adic;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link Adic}
 */
@Getter
@Setter
public class AdicDto implements Serializable {
    Integer id;
    String cod;
    Integer op;
    String nom;
    BigDecimal valor;
    BigDecimal cant;
    LocalDate fecha;
}

