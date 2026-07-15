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
        "id", "OP", "tdespacho", "nom_cliente", "nom_op", "fecha_ini_op", "FECHA_T",
        "COD", "UBIC", "nom_material", "cantidad_solicitada", "cantidad_comprada",
        "EXIST", "UD", "cantidad_entregada", "dif_cantidad", "cos_est_unitario",
        "cos_real_unitario", "costo_solicitado", "costo_entregado", "dif_costo",
        "ca_cla-ni1_nombre", "ca_cla-ni2_nombre", "ca_cla-ni3_nombre",
        "ca_cla-ni4_nombre", "ca_cla-ni5_nombre", "CANTP", "CANTE", "Expr3", "Expr1", "Expr2"
})
public class ViMaterialesDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("OP")
    private Integer OP;

    @JsonProperty("tdespacho")
    @Size(max = 250)
    private String tdespacho;

    @JsonProperty("nom_cliente")
    @Size(max = 120)
    private String nom_cliente;

    @JsonProperty("nom_op")
    @Size(max = 100)
    private String nom_op;

    @JsonProperty("fecha_ini_op")
    private Instant fecha_ini_op;

    @JsonProperty("FECHA_T")
    private Instant FECHA_T;

    @JsonProperty("COD")
    @Size(max = 20)
    private String COD;

    @JsonProperty("UBIC")
    @Size(max = 6)
    private String UBIC;

    @JsonProperty("nom_material")
    @Size(max = 100)
    private String nom_material;

    @JsonProperty("cantidad_solicitada")
    private BigDecimal cantidad_solicitada;

    @JsonProperty("cantidad_comprada")
    @NotNull
    private BigDecimal cantidad_comprada;

    @JsonProperty("EXIST")
    private BigDecimal EXIST;

    @JsonProperty("UD")
    @Size(max = 2)
    private String UD;

    @JsonProperty("cantidad_entregada")
    private BigDecimal cantidad_entregada;

    @JsonProperty("dif_cantidad")
    private BigDecimal dif_cantidad;

    @JsonProperty("cos_est_unitario")
    private BigDecimal cos_est_unitario;

    @JsonProperty("cos_real_unitario")
    private BigDecimal cos_real_unitario;

    @JsonProperty("costo_solicitado")
    private BigDecimal costo_solicitado;

    @JsonProperty("costo_entregado")
    private BigDecimal costo_entregado;

    @JsonProperty("dif_costo")
    private BigDecimal dif_costo;

    @JsonProperty("ca_cla-ni1_nombre")
    @Size(max = 30)
    private String ca_cla_ni1_nombre;

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

    @JsonProperty("CANTP")
    private BigDecimal CANTP;

    @JsonProperty("CANTE")
    private BigDecimal CANTE;

    @JsonProperty("Expr3")
    @NotNull
    @Size(max = 50)
    private String Expr3;

    @JsonProperty("Expr1")
    @NotNull
    @Size(max = 80)
    private String Expr1;

    @JsonProperty("Expr2")
    @NotNull
    @Size(max = 50)
    private String Expr2;
}

