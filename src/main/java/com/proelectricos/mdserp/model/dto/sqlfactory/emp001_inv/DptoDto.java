package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@JsonPropertyOrder({
        "COD", "NOM", "CMO", "CCF", "SMO", "SCF", "ACTIVO", "MUERTO",
        "ESTADO", "EXTRAS", "RETRA", "ALMUE", "PARADA", "POSIBLE", "NCONTROL",
        "INI_H", "INI_M", "TELEFONO", "HORAMAQ", "INACTIVA", "FINACTIVA",
        "PROAREA", "CMONIIF", "CCFNIIF", "PVENTADPTO", "CLASIFICA", "FACTOR",
        "AJUSTECEN","ID", "JIKKOID", "ACTIVOC", "BODDEFECTO"
})
public class DptoDto implements Serializable {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("COD")
    @Size(max = 4)
    private String COD;

    @JsonProperty("NOM")
    @Size(max = 35)
    private String NOM;

    @JsonProperty("CMO")
    private BigDecimal CMO;

    @JsonProperty("CCF")
    private BigDecimal CCF;

    @JsonProperty("SMO")
    private BigDecimal SMO;

    @JsonProperty("SCF")
    private BigDecimal SCF;

    @JsonProperty("ACTIVO")
    private BigDecimal ACTIVO;

    @JsonProperty("MUERTO")
    private BigDecimal MUERTO;

    @JsonProperty("ESTADO")
    private BigDecimal ESTADO;

    @JsonProperty("EXTRAS")
    private BigDecimal EXTRAS;

    @JsonProperty("RETRA")
    private BigDecimal RETRA;

    @JsonProperty("ALMUE")
    private BigDecimal ALMUE;

    @JsonProperty("PARADA")
    private BigDecimal PARADA;

    @JsonProperty("POSIBLE")
    private BigDecimal POSIBLE;

    @JsonProperty("NCONTROL")
    private BigDecimal NCONTROL;

    @JsonProperty("INI_H")
    private Integer INI_H;

    @JsonProperty("INI_M")
    private Integer INI_M;

    @JsonProperty("TELEFONO")
    @Size(max = 15)
    private String TELEFONO;

    @JsonProperty("HORAMAQ")
    private BigDecimal HORAMAQ;

    @JsonProperty("INACTIVA")
    private BigDecimal INACTIVA;

    @JsonProperty("FINACTIVA")
    private LocalDate FINACTIVA;

    @JsonProperty("PROAREA")
    private BigDecimal PROAREA;

    @JsonProperty("CMONIIF")
    private BigDecimal CMONIIF;

    @JsonProperty("CCFNIIF")
    private BigDecimal CCFNIIF;

    @JsonProperty("PVENTADPTO")
    private BigDecimal PVENTADPTO;

    @JsonProperty("CLASIFICA")
    @Size(max = 2)
    private String CLASIFICA;

    @JsonProperty("FACTOR")
    private BigDecimal FACTOR;

    @JsonProperty("AJUSTECEN")
    private Integer AJUSTECEN;

    @JsonProperty("JIKKOID")
    private Integer JIKKOID;

    @JsonProperty("ACTIVOC")
    private Character ACTIVOC;

    @JsonProperty("BODDEFECTO")
    @Size(max = 2)
    private String BODDEFECTO;
}

