package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoEspeciale}
 */
@Getter
@Setter
public class CoEspecialeDto implements Serializable {
    @Size(max = 20)
    private String cod;
    @Size(max = 100)
    private String nom;
    @Size(max = 2)
    private String ud;
    @Size(max = 2)
    private String grup;
    private BigDecimal saldo;
    @Size(max = 2)
    private String alma;
    @Size(max = 30)
    private String bodega;
    private BigDecimal exist;
    private BigDecimal costounitario;
    @NotNull
    @Size(max = 50)
    private String linea;
    @NotNull
    @Size(max = 80)
    private String clase;
    @NotNull
    @Size(max = 50)
    private String grupo;
    @NotNull
    @Size(max = 60)
    private String nivel5;
}


