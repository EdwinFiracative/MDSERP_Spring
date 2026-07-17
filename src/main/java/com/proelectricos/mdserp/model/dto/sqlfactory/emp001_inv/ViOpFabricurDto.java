package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViOpFabricur}
 */
@Getter
@Setter
public class ViOpFabricurDto implements Serializable {
    private Integer OP;
    private Character MODELO;
    @Size(max = 20)
    private String COD;
    @Size(max = 100)
    private String NOM;
    @Size(max = 2)
    private String UD;
    @Size(max = 2)
    private String GRUP;
    private BigDecimal CANTP;
    private BigDecimal CANTE;
    private LocalDate FECHA_I;
    private LocalDate FECHA_T;
    private Character ESTADO;
    @NotNull
    @Size(max = 50)
    private String ca_cla_ni4_nombre;
    @Size(max = 20)
    private String codmp;
    @Size(max = 100)
    private String NOM_MP;
    @Size(max = 2)
    private String UD_MP;
    @Size(max = 2)
    private String GRP_MP;
    private BigDecimal CANT;
    @Size(max = 20)
    private String CodK;
    private BigDecimal SumCantK;
}


