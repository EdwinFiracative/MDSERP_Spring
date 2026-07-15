package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@JsonPropertyOrder({
         "OP", "CODOP", "CANTP", "CANTE", "FECHA_I", "ESTADO", "Perido",
        "CODMP", "NOM", "CANT", "CMASTER", "CantidaK", "VMPK", "CANTDISP",
        "Negocio", "Clase", "Linea", "Grupo", "Nivel5"
})
public class MaterialesDto implements Serializable {

    /*@JsonProperty("id")
    private Long id;*/

    @JsonProperty("OP")
    private Integer OP;

    @JsonProperty("CODOP")
    @Size(max = 20)
    private String CODOP;

    @JsonProperty("CANTP")
    private BigDecimal CANTP;

    @JsonProperty("CANTE")
    private BigDecimal CANTE;

    @JsonProperty("FECHA_I")
    private Instant FECHA_I;

    @JsonProperty("ESTADO")
    private Character ESTADO;

    @JsonProperty("Perido")
    @Size(max = 7)
    private String Perido;

    @JsonProperty("CODMP")
    @Size(max = 20)
    private String CODMP;

    @JsonProperty("NOM")
    @Size(max = 100)
    private String NOM;

    @JsonProperty("CANT")
    private BigDecimal CANT;

    @JsonProperty("CMASTER")
    private BigDecimal CMASTER;

    @JsonProperty("CantidaK")
    @NotNull
    private BigDecimal CantidaK;

    @JsonProperty("VMPK")
    @NotNull
    private BigDecimal VMPK;

    @JsonProperty("CANTDISP")
    private BigDecimal CANTDISP;

    @JsonProperty("Negocio")
    @NotNull
    @Size(max = 30)
    private String Negocio;

    @JsonProperty("Clase")
    @NotNull
    @Size(max = 50)
    private String Clase;

    @JsonProperty("Linea")
    @NotNull
    @Size(max = 80)
    private String Linea;

    @JsonProperty("Grupo")
    @NotNull
    @Size(max = 50)
    private String Grupo;

    @JsonProperty("Nivel5")
    @NotNull
    @Size(max = 60)
    private String Nivel5;
}

