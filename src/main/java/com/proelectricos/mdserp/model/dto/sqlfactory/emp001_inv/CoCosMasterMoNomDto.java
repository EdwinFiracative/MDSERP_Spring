package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CoCosMasterMoNomDto implements Serializable {

    @Size(max = 20)
    @JsonProperty("cac_co_cos_mp_cod_pro")
    private String cacCoCosMpCodPro;

    @Size(max = 60)
    @JsonProperty("cac_cod_cot")
    private String cacCodCot;

    @Size(max = 100)
    @JsonProperty("cac_nom_prod")
    private String cacNomProd;

    @JsonProperty("modelo")
    private Character modelo;

    @Size(max = 30)
    @NotNull
    @JsonProperty("ca_cla-ni1_nombre")
    private String caClaNi1Nombre;

    @Size(max = 50)
    @NotNull
    @JsonProperty("ca_cla-ni2_nombre")
    private String caClaNi2Nombre;

    @Size(max = 80)
    @NotNull
    @JsonProperty("ca_cla-ni3_nombre")
    private String caClaNi3Nombre;

    @Size(max = 50)
    @NotNull
    @JsonProperty("ca_cla-ni4_nombre")
    private String caClaNi4Nombre;

    @Size(max = 60)
    @NotNull
    @JsonProperty("ca_cla-ni5_nombre")
    private String caClaNi5Nombre;

    @Size(max = 20)
    @JsonProperty("cod")
    private String cod;

    @Size(max = 100)
    @JsonProperty("nom")
    private String nom;

    @JsonProperty("lmin")
    private BigDecimal lmin;

    @JsonProperty("sum_cant")
    private BigDecimal sumCant;

    @Size(max = 3)
    @JsonProperty("ud")
    private String ud;

    @JsonProperty("cac_cant_unit")
    private BigDecimal cacCantUnit;

    @JsonProperty("cac_cos_uni_final")
    private BigDecimal cacCosUniFinal;

    @JsonProperty("cac_cos_pro_unit")
    private BigDecimal cacCosProUnit;

    @Size(max = 30)
    @JsonProperty("DESCRIP")
    private String descrip;

    @Size(max = 2)
    @JsonProperty("NUM")
    private String num;
}
