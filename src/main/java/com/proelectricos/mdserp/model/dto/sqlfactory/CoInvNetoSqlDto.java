package com.proelectricos.mdserp.model.dto.sqlfactory;

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
        "COD", "NOM", "UD", "GRUP", "PEDIDO", "PROCESO", "BOD31", "BOD32", "BOD20", "BOD39",
        "SMAX", "SMIN", "UNID_OP", "LOTE_OPT", "MODELO", "CONSUMO_PROMEDIO", "NEGOCIO",
        "LINEA", "SUBLINEA", "GRUPO", "ca_cla_ni1_id", "ca_cla_ni2_id", "ca_cla_ni3_id",
        "ca_cla_ni4_id", "ca_cla_ni5_id"
})
public class CoInvNetoSqlDto implements Serializable {

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

    @JsonProperty("PEDIDO")
    @NotNull
    private BigDecimal PEDIDO;

    @JsonProperty("PROCESO")
    @NotNull
    private BigDecimal PROCESO;

    @JsonProperty("BOD31")
    @NotNull
    private BigDecimal BOD31;

    @JsonProperty("BOD32")
    @NotNull
    private BigDecimal BOD32;

    @JsonProperty("BOD20")
    @NotNull
    private BigDecimal BOD20;

    @JsonProperty("BOD39")
    @NotNull
    private BigDecimal BOD39;

    @JsonProperty("SMAX")
    private BigDecimal SMAX;

    @JsonProperty("SMIN")
    private BigDecimal SMIN;

    @JsonProperty("UNID_OP")
    private BigDecimal UNID_OP;

    @JsonProperty("LOTE_OPT")
    private BigDecimal LOTE_OPT;

    @JsonProperty("MODELO")
    private String MODELO;

    @JsonProperty("CONSUMO_PROMEDIO")
    private BigDecimal CONSUMO_PROMEDIO;

    @JsonProperty("NEGOCIO")
    @Size(max = 50)
    private String NEGOCIO;

    @JsonProperty("LINEA")
    @Size(max = 80)
    private String LINEA;

    @JsonProperty("SUBLINEA")
    @Size(max = 50)
    private String SUBLINEA;

    @JsonProperty("GRUPO")
    @Size(max = 60)
    private String GRUPO;

    @JsonProperty("ca_cla_ni1_id")
    private Integer ca_cla_ni1_id;

    @JsonProperty("ca_cla_ni2_id")
    private Integer ca_cla_ni2_id;

    @JsonProperty("ca_cla_ni3_id")
    private Integer ca_cla_ni3_id;

    @JsonProperty("ca_cla_ni4_id")
    private Integer ca_cla_ni4_id;

    @JsonProperty("ca_cla_ni5_id")
    private Integer ca_cla_ni5_id;
}