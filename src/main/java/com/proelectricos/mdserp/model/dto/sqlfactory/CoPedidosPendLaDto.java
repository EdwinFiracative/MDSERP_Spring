package com.proelectricos.mdserp.model.dto.sqlfactory;

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
        "FECHA", "NUM", "CLIENTE", "NomCliente", "CodVend", "NomVend", "ciu", "zona",
        "ENTREGA", "COD", "NOM", "UD", "GRUP", "CANT", "COSTO", "PEND", "ESTADO",
        "NETO", "ORDEN", "NCOD", "CONDIC", "DESPACHO", "CANTPEND", "VR PENDIENTE",
        "Negocio", "Linea_Neg_nombre", "SubLinea_Neg_nom", "Grupo_Neg_nom"
})
public class CoPedidosPendLaDto implements Serializable {

    @JsonProperty("FECHA")
    private LocalDate FECHA;

    @JsonProperty("NUM")
    @Size(max = 7)
    private String NUM;

    @JsonProperty("CLIENTE")
    @Size(max = 15)
    private String CLIENTE;

    @JsonProperty("NomCliente")
    @Size(max = 200)
    private String NomCliente;

    @JsonProperty("CodVend")
    @Size(max = 5)
    private String CodVend;

    @JsonProperty("NomVend")
    @Size(max = 30)
    private String NomVend;

    @JsonProperty("ciu")
    @Size(max = 20)
    private String ciu;

    @JsonProperty("zona")
    @Size(max = 3)
    private String zona;

    @JsonProperty("ENTREGA")
    private LocalDate ENTREGA;

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

    @JsonProperty("CANT")
    private BigDecimal CANT;

    @JsonProperty("COSTO")
    private BigDecimal COSTO;

    @JsonProperty("PEND")
    private BigDecimal PEND;

    @JsonProperty("ESTADO")
    private Character ESTADO;

    @JsonProperty("NETO")
    private BigDecimal NETO;

    @JsonProperty("ORDEN")
    @Size(max = 50)
    private String ORDEN;

    @JsonProperty("NCOD")
    @Size(max = 6)
    private String NCOD;

    @JsonProperty("CONDIC")
    @Size(max = 30)
    private String CONDIC;

    @JsonProperty("DESPACHO")
    @NotNull
    private BigDecimal DESPACHO;

    @JsonProperty("CANTPEND")
    private BigDecimal CANTPEND;

    @JsonProperty("VR PENDIENTE")
    private BigDecimal VR_PENDIENTE;

    @JsonProperty("Negocio")
    @NotNull
    @Size(max = 50)
    private String Negocio;

    @JsonProperty("Linea_Neg_nombre")
    @NotNull
    @Size(max = 80)
    private String Linea_Neg_nombre;

    @JsonProperty("SubLinea_Neg_nom")
    @NotNull
    @Size(max = 50)
    private String SubLinea_Neg_nom;

    @JsonProperty("Grupo_Neg_nom")
    @NotNull
    @Size(max = 60)
    private String Grupo_Neg_nom;
}
