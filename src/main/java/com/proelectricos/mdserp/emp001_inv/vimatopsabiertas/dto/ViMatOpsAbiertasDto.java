package com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@JsonPropertyOrder({
        "op", "Ref", "NombreProducto", "ud", "grup", "cstd", "tipo", "cantp", "cante",
        "FECHA_I", "FECHA_T", "ESTADO", "codMaterial", "NombreMaterial", "modelo",
        "cantMaster", "valorMaster", "cantKardex", "valorK",
        "negocio", "clase", "linea", "grupo", "nivel5"
})
public class ViMatOpsAbiertasDto implements Serializable {

    @JsonProperty("op")
    private Integer op;

    @JsonProperty("Ref")
    private String ref;

    @JsonProperty("NombreProducto")
    private String nombreProducto;

    @JsonProperty("ud")
    private String ud;

    @JsonProperty("grup")
    private String grup;

    @JsonProperty("cstd")
    private BigDecimal cstd;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("cantp")
    private BigDecimal cantp;

    @JsonProperty("cante")
    private BigDecimal cante;

    @JsonProperty("FECHA_I")
    private LocalDate fechaI;

    @JsonProperty("FECHA_T")
    private LocalDate fechaT;

    @JsonProperty("ESTADO")
    private String estado;

    @JsonProperty("codMaterial")
    private String codMaterial;

    @JsonProperty("NombreMaterial")
    private String nombreMaterial;

    @JsonProperty("modelo")
    private String modelo;

    @JsonProperty("cantMaster")
    private BigDecimal cantMaster;

    @JsonProperty("valorMaster")
    private BigDecimal valorMaster;

    @JsonProperty("cantKardex")
    private BigDecimal cantKardex;

    @JsonProperty("valorK")
    private BigDecimal valorK;

    @JsonProperty("negocio")
    private String negocio;

    @JsonProperty("clase")
    private String clase;

    @JsonProperty("linea")
    private String linea;

    @JsonProperty("grupo")
    private String grupo;

    @JsonProperty("nivel5")
    private String nivel5;
}
