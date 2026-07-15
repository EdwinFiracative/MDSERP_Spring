package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

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
        "NUM", "ORDEN", "FECHA", "CLIENTE", "NIT", "NOMBRE DEL CLIENTE",
        "CIU", "VENDEDOR", "NOMBRE DEL VENDEDOR", "COSTO ITEM", "ESTADO",
        "Negocio", "Clase"
})
public class CoPedPendAprobDto implements Serializable {

    /*@JsonProperty("id")
    private Long id;*/

    @JsonProperty("NUM")
    @Size(max = 7)
    private String num;

    @JsonProperty("ORDEN")
    @Size(max = 50)
    private String orden;

    @JsonProperty("FECHA")
    private Instant fecha;

    @JsonProperty("CLIENTE")
    @Size(max = 15)
    private String cliente;

    @JsonProperty("NIT")
    @Size(max = 15)
    private String nit;

    @JsonProperty("NOMBRE DEL CLIENTE")
    @Size(max = 120)
    private String nombreDelCliente;

    @JsonProperty("CIU")
    @Size(max = 20)
    private String ciu;

    @JsonProperty("VENDEDOR")
    @Size(max = 5)
    private String vendedor;

    @JsonProperty("NOMBRE DEL VENDEDOR")
    @Size(max = 30)
    private String nombreDelVendedor;

    @JsonProperty("COSTO ITEM")
    private BigDecimal costoItem;

    @JsonProperty("ESTADO")
    private Character estado;

    @JsonProperty("Negocio")
    @NotNull
    @Size(max = 30)
    private String negocio;

    @JsonProperty("Clase")
    @NotNull
    @Size(max = 50)
    private String clase;
}

