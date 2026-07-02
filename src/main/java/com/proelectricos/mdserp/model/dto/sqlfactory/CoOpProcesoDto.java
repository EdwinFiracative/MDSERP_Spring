package com.proelectricos.mdserp.model.dto.sqlfactory;

import com.proelectricos.mdserp.model.entity.sqlfactory.CoOpProceso;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDate;

/**
 * DTO for {@link CoOpProceso}
 */
@Getter
@Setter
public class CoOpProcesoDto implements Serializable {

    //private Long id;
    private Integer op;
    private LocalDate fechaI;
    @Size(max = 20)
    private String cod;
    @Size(max = 100)
    private String nom;
    @Size(max = 2)
    private String grup;
    @Size(max = 40)
    private String nomGrup;
    @Size(max = 40)
    private String nota;
    private BigDecimal cantp;
    private BigDecimal cante;
    private BigDecimal saldo;
    private LocalDate fechaT;
    private BigDecimal costoApl;
    private BigDecimal costoLiq;
    private BigDecimal costoProc;
    @NotNull
    @Size(max = 50)
    private String negocio;
    @NotNull
    @Size(max = 80)
    private String linea;
    @NotNull
    @Size(max = 50)
    private String subLinea;
    @NotNull
    @Size(max = 60)
    private String grupo;
    private Integer anio;
    private Integer mes;
    private BigDecimal sumaDecacCant;
    private Integer cantUni;
    private BigDecimal sumCosUnit;
    private LocalDate vence;
}
