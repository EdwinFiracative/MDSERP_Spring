package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "co_cos_master_MO_nom",   catalog = "EMP001_INV", schema = "dbo")
public class CoCosMasterMoNom {

    @Id
    @Size(max = 20)
    @Column(name = "cac_co_cos_mp_cod_pro", length = 20)
    private String cacCoCosMpCodPro;

    @Size(max = 60)
    @Column(name = "cac_cod_cot", length = 60)
    private String cacCodCot;

    @Size(max = 100)
    @Column(name = "cac_nom_prod", length = 100)
    private String cacNomProd;

    @Column(name = "modelo")
    private Character modelo;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni1_nombre\"", nullable = false, length = 30)
    private String caClaNi1Nombre;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni2_nombre\"", nullable = false, length = 50)
    private String caClaNi2Nombre;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni3_nombre\"", nullable = false, length = 80)
    private String caClaNi3Nombre;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", nullable = false, length = 50)
    private String caClaNi4Nombre;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni5_nombre\"", nullable = false, length = 60)
    private String caClaNi5Nombre;

    @Size(max = 20)
    @Column(name = "cod", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "lmin", precision = 16, scale = 4)
    private BigDecimal lmin;

    @Column(name = "sum_cant", precision = 38, scale = 4)
    private BigDecimal sumCant;

    @Size(max = 3)
    @Column(name = "ud", length = 3)
    private String ud;

    @Column(name = "cac_cant_unit", precision = 38, scale = 6)
    private BigDecimal cacCantUnit;

    @Column(name = "cac_cos_uni_final", precision = 38, scale = 6)
    private BigDecimal cacCosUniFinal;

    @Column(name = "cac_cos_pro_unit", precision = 38, scale = 6)
    private BigDecimal cacCosProUnit;

    @Size(max = 30)
    @Column(name = "DESCRIP", length = 30)
    private String descrip;

    @Size(max = 2)
    @Column(name = "NUM", length = 2)
    private String num;


}