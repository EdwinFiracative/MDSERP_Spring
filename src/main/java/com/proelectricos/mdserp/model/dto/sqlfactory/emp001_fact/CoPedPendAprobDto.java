package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@JsonPropertyOrder({
        "NUM", "ORDEN", "FECHA", "CLIENTE", "NIT", "NOMBRE DEL CLIENTE",
        "CIU", "VENDEDOR", "NOMBRE DEL VENDEDOR", "COSTO ITEM", "ESTADO",
        "Negocio", "Clase"
})
public class CoPedPendAprobDto implements Serializable {

    /*@JsonProperty("id")
    private Long id;*/

    @JsonProperty("NUM")
    @Size(max = 7)
    private String NUM;

    @JsonProperty("ORDEN")
    @Size(max = 50)
    private String ORDEN;

    @JsonProperty("FECHA")
    private LocalDate FECHA;

    @JsonProperty("CLIENTE")
    @Size(max = 15)
    private String CLIENTE;

    @JsonProperty("NIT")
    @Size(max = 15)
    private String NIT;

    @JsonProperty("NOMBRE DEL CLIENTE")
    @Size(max = 120)
    private String NOMBRE_DEL_CLIENTE;

    @JsonProperty("CIU")
    @Size(max = 20)
    private String CIU;

    @JsonProperty("VENDEDOR")
    @Size(max = 5)
    private String VENDEDOR;

    @JsonProperty("NOMBRE DEL VENDEDOR")
    @Size(max = 30)
    private String NOMBRE_DEL_VENDEDOR;

    @JsonProperty("COSTO ITEM")
    private BigDecimal COSTO_ITEM;

    @JsonProperty("ESTADO")
    private Character ESTADO;

    @JsonProperty("Negocio")
    @NotNull
    @Size(max = 30)
    private String Negocio;

    @JsonProperty("Clase")
    @NotNull
    @Size(max = 50)
    private String Clase;
}

