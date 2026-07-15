package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonPropertyOrder({
         "OC", "CPROV", "NOMBRE", "FECHA CREACION", "FECHA PLANEADA",
        "COMO", "VAN", "ESTADO", "COD", "NOM", "UD", "GRUP", "CANTP", "CANTE",
        "ESTAD", "CANT_K", "PNIT", "CANT_PEND", "NEGOCIO", "LINEA", "CLASE",
        "GRUPO", "NIVEL5"
})
public class CoCompras1Dto implements Serializable {

  /*  @JsonProperty("id")
    private Long id;*/

    @JsonProperty("OC")
    private Integer OC;

    @JsonProperty("CPROV")
    @Size(max = 15)
    private String CPROV;

    @JsonProperty("NOMBRE")
    @Size(max = 60)
    private String NOMBRE;

    @JsonProperty("FECHA CREACION")
    @Size(max = 4000)
    private String FECHA_CREACION;

    @JsonProperty("FECHA PLANEADA")
    @Size(max = 4000)
    private String FECHA_PLANEADA;

    @JsonProperty("COMO")
    private Integer COMO;

    @JsonProperty("VAN")
    private Integer VAN;

    @JsonProperty("ESTADO")
    private Character ESTADO;

    @JsonProperty("COD")
    @Size(max = 20)
    private String COD;

    @JsonProperty("NOM")
    @Size(max = 100)
    private String NOM;

    @JsonProperty("UD")
    @Size(max = 2)
    private String UD;

    @JsonProperty("GRUP")
    @Size(max = 2)
    private String GRUP;

    @JsonProperty("CANTP")
    private BigDecimal CANTP;

    @JsonProperty("CANTE")
    private BigDecimal CANTE;

    @JsonProperty("ESTAD")
    private Character ESTAD;

    @JsonProperty("CANT_K")
    @NotNull
    private BigDecimal CANT_K;

    @JsonProperty("PNIT")
    @Size(max = 30)
    private String PNIT;

    @JsonProperty("CANT_PEND")
    private BigDecimal CANT_PEND;

    @JsonProperty("NEGOCIO")
    @NotNull
    @Size(max = 30)
    private String NEGOCIO;

    @JsonProperty("LINEA")
    @NotNull
    @Size(max = 50)
    private String LINEA;

    @JsonProperty("CLASE")
    @NotNull
    @Size(max = 80)
    private String CLASE;

    @JsonProperty("GRUPO")
    @NotNull
    @Size(max = 50)
    private String GRUPO;

    @JsonProperty("NIVEL5")
    @NotNull
    @Size(max = 60)
    private String NIVEL5;
}

