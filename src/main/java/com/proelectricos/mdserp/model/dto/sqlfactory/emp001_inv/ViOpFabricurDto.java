package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.ViOpFabricur}
 */
@Getter
@Setter
public class ViOpFabricurDto implements Serializable {
    private Integer op;
    private Character modelo;
    @Size(max = 20)
    private String cod;
    @Size(max = 100)
    private String nom;
    @Size(max = 2)
    private String ud;
    @Size(max = 2)
    private String grup;
    private BigDecimal cantp;
    private BigDecimal cante;
    private Instant fechaI;
    private Instant fechaT;
    private Character estado;
    @NotNull
    @Size(max = 50)
    private String caClaNi4Nombre;
    @Size(max = 20)
    private String codmp;
    @Size(max = 100)
    private String nomMp;
    @Size(max = 2)
    private String udMp;
    @Size(max = 2)
    private String grpMp;
    private BigDecimal cant;
    @Size(max = 20)
    private String codK;
    private BigDecimal sumCantK;
}


