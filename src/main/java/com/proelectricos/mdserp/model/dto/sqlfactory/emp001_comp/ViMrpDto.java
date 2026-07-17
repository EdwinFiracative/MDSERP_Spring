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
        "COD", "NOM", "inv_existencia", "afi_compras", "SMIN", "solicitud_op",
        "comprar_unid_almacen", "UD", "comprar_unid_compras", "stock_max_uni_compras",
        "UDA", "CSTD", "costo_compra", "ca_cla-ni2_nombre", "ca_cla-ni3_nombre",
        "ca_cla-ni4_nombre", "ca_cla-ni5_nombre"
})
public class ViMrpDto implements Serializable {

    @JsonProperty("COD")
    @Size(max = 20)
    private String COD;

    @JsonProperty("NOM")
    @Size(max = 100)
    private String NOM;

    @JsonProperty("inv_existencia")
    @NotNull
    private BigDecimal inv_existencia;

    @JsonProperty("afi_compras")
    private BigDecimal afi_compras;

    @JsonProperty("SMIN")
    private BigDecimal SMIN;

    @JsonProperty("solicitud_op")
    @NotNull
    private BigDecimal solicitud_op;

    @JsonProperty("comprar_unid_almacen")
    private BigDecimal comprar_unid_almacen;

    @JsonProperty("UD")
    @Size(max = 2)
    private String UD;

    @JsonProperty("comprar_unid_compras")
    private BigDecimal comprar_unid_compras;

    @JsonProperty("stock_max_uni_compras")
    private BigDecimal stock_max_uni_compras;

    @JsonProperty("UDA")
    @Size(max = 2)
    private String UDA;

    @JsonProperty("CSTD")
    private BigDecimal CSTD;

    @JsonProperty("costo_compra")
    private BigDecimal costo_compra;

    @JsonProperty("ca_cla-ni2_nombre")
    @Size(max = 50)
    private String ca_cla_ni2_nombre;

    @JsonProperty("ca_cla-ni3_nombre")
    @Size(max = 80)
    private String ca_cla_ni3_nombre;

    @JsonProperty("ca_cla-ni4_nombre")
    @Size(max = 50)
    private String ca_cla_ni4_nombre;

    @JsonProperty("ca_cla-ni5_nombre")
    @Size(max = 60)
    private String ca_cla_ni5_nombre;
}
