package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

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
        "COD", "NOM", "UD", "GRUP", "CATEG", "CLASE_PROD", "CODMP", "REQUISITO",
        "EXIST", "CANT_OC", "SMAX", "COMPRAR", "VALOR Existencia", "SMIN",
        "Costo repo", "Costo STD", "Cons Prom", "Negocio", "Linea", "Clase",
        "Grupo", "Nivel5", "LEAD TIMES", "PRM_CONS", "DESVSTD"
})
public class CoInvNetoMpDto implements Serializable {

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

    @JsonProperty("CATEG")
    private Character CATEG;

    @JsonProperty("CLASE_PROD")
    @Size(max = 40)
    private String CLASE_PROD;

    @JsonProperty("CODMP")
    @Size(max = 20)
    private String CODMP;

    @JsonProperty("REQUISITO")
    @NotNull
    private BigDecimal REQUISITO;

    @JsonProperty("EXIST")
    private BigDecimal EXIST;

    @JsonProperty("CANT_OC")
    private BigDecimal CANT_OC;

    @JsonProperty("SMAX")
    private BigDecimal SMAX;

    @JsonProperty("COMPRAR")
    private BigDecimal COMPRAR;

    @JsonProperty("VALOR Existencia")
    private BigDecimal VALOR_Existencia;

    @JsonProperty("SMIN")
    private BigDecimal SMIN;

    @JsonProperty("Costo repo")
    private BigDecimal Costo_repo;

    @JsonProperty("Costo STD")
    private BigDecimal Costo_STD;

    @JsonProperty("Cons Prom")
    private BigDecimal Cons_Prom;

    @JsonProperty("Negocio")
    @NotNull
    @Size(max = 30)
    private String Negocio;

    @JsonProperty("Linea")
    @NotNull
    @Size(max = 50)
    private String Linea;

    @JsonProperty("Clase")
    @NotNull
    @Size(max = 80)
    private String Clase;

    @JsonProperty("Grupo")
    @NotNull
    @Size(max = 50)
    private String Grupo;

    @JsonProperty("Nivel5")
    @NotNull
    @Size(max = 60)
    private String Nivel5;

    @JsonProperty("LEAD TIMES")
    private Integer LEAD_TIMES;

    @JsonProperty("PRM_CONS")
    private BigDecimal PRM_CONS;

    @JsonProperty("DESVSTD")
    private Double DESVSTD;
}
