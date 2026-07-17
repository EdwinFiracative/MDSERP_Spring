package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

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
        "COD", "UBIC", "cantidad_a_comprar", "cos_est_unitario", "OP",
        "Mat_Elec_Plan", "pedido", "CMR", "cantidad_solicitada", "cantidad_entregada",
        "cantidad_pendiente", "cantidad_comprada", "EXIST", "nom_material",
        "nom_op", "proyecto", "cliente", "fecha_ini_op", "FECHA_T", "CANTP",
        "CANTE", "cos_real_unitario", "costo_solicitado", "costo_entregado", "dif_costo",
        "ca_cla-ni1_nombre", "ca_cla-ni2_nombre", "ca_cla-ni3_nombre",
        "ca_cla-ni4_nombre", "ca_cla-ni5_nombre"
})
public class ViMaterialesOPDto implements Serializable {

    /*@JsonProperty("id")
    private Long id;*/

    @JsonProperty("COD")
    @Size(max = 20)
    private String COD;

    @JsonProperty("UBIC")
    @Size(max = 6)
    private String UBIC;

    @JsonProperty("cantidad_a_comprar")
    private BigDecimal cantidad_a_comprar;

    @JsonProperty("cos_est_unitario")
    private BigDecimal cos_est_unitario;

    @JsonProperty("OP")
    private Integer OP;

    @JsonProperty("Mat_Elec_Plan")
    private LocalDate Mat_Elec_Plan;

    @JsonProperty("pedido")
    @Size(max = 7)
    private String pedido;

    @JsonProperty("CMR")
    private BigDecimal CMR;

    @JsonProperty("cantidad_solicitada")
    private BigDecimal cantidad_solicitada;

    @JsonProperty("cantidad_entregada")
    private BigDecimal cantidad_entregada;

    @JsonProperty("cantidad_pendiente")
    private BigDecimal cantidad_pendiente;

    @JsonProperty("cantidad_comprada")
    @NotNull
    private BigDecimal cantidad_comprada;

    @JsonProperty("EXIST")
    private BigDecimal EXIST;

    @JsonProperty("nom_material")
    @Size(max = 100)
    private String nom_material;

    @JsonProperty("nom_op")
    @Size(max = 100)
    private String nom_op;

    @JsonProperty("proyecto")
    @NotNull
    @Size(max = 250)
    private String proyecto;

    @JsonProperty("cliente")
    @NotNull
    @Size(max = 120)
    private String cliente;

    @JsonProperty("fecha_ini_op")
    private LocalDate fecha_ini_op;

    @JsonProperty("FECHA_T")
    private LocalDate FECHA_T;

    @JsonProperty("CANTP")
    private BigDecimal CANTP;

    @JsonProperty("CANTE")
    private BigDecimal CANTE;

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
}

