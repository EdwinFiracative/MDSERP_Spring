package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ViOpMasterTimeCecoDto implements Serializable {

    /*private Long id;*/

    @JsonProperty("OP")
    private Integer op;

    @Size(max = 20)
    @JsonProperty("COD")
    private String cod;

    @Size(max = 100)
    @JsonProperty("NOM")
    private String nom;

    @Size(max = 2)
    @JsonProperty("UD")
    private String ud;

    @Size(max = 2)
    @JsonProperty("GRUP")
    private String grup;

    @JsonProperty("CANTP")
    private BigDecimal cantp;

    @JsonProperty("CANTE")
    private BigDecimal cante;

    @JsonProperty("FECHA_I")
    private Instant fechaI;

    @JsonProperty("FECHA_T")
    private Instant fechaT;

    @JsonProperty("ESTADO")
    private Character estado;

    @Size(max = 4)
    @JsonProperty("CECO")
    private String ceco;

    @Size(max = 35)
    @JsonProperty("NOMCECO")
    private String nomceco;

    @JsonProperty("CANTIDAD HRS")
    private BigDecimal cantidadHrs;

    @JsonProperty("VLR CMO")
    private BigDecimal vlrCmo;

    @JsonProperty("VLR CCF")
    private BigDecimal vlrCcf;

    @Size(max = 30)
    @NotNull
    @JsonProperty("Negocio")
    private String negocio;

    @Size(max = 50)
    @NotNull
    @JsonProperty("Linea")
    private String linea;

    @Size(max = 80)
    @NotNull
    @JsonProperty("Clase")
    private String clase;

    @Size(max = 50)
    @NotNull
    @JsonProperty("Grupo")
    private String grupo;

    @Size(max = 60)
    @NotNull
    @JsonProperty("Nivel5")
    private String nivel5;
}
