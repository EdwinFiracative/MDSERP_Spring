package com.proelectricos.mdserp.model.entity.sqlfactory;

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
@Table(name = "CO_INV_NETO_SQL" ,  catalog = "EMP001_INV", schema = "dbo")
public class CoInvNetoSql {

    @Id
    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String NOM;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String UD;

    @Size(max = 2)
    @Column(name = "GRUP", length = 2)
    private String GRUP;

    @NotNull
    @Column(name = "PEDIDO", nullable = false, precision = 38, scale = 4)
    private BigDecimal PEDIDO;

    @NotNull
    @Column(name = "PROCESO", nullable = false, precision = 38, scale = 4)
    private BigDecimal PROCESO;

    @NotNull
    @Column(name = "BOD31", nullable = false, precision = 38, scale = 4)
    private BigDecimal BOD31;

    @NotNull
    @Column(name = "BOD32", nullable = false, precision = 38, scale = 4)
    private BigDecimal BOD32;

    @NotNull
    @Column(name = "BOD20", nullable = false, precision = 38, scale = 4)
    private BigDecimal BOD20;

    @NotNull
    @Column(name = "BOD39", nullable = false, precision = 38, scale = 4)
    private BigDecimal BOD39;

    @Column(name = "SMAX", precision = 14, scale = 4)
    private BigDecimal SMAX;

    @Column(name = "SMIN", precision = 14, scale = 4)
    private BigDecimal SMIN;

    @Column(name = "UNID_OP", precision = 38, scale = 4)
    private BigDecimal UNID_OP;

    @Column(name = "LOTE_OPT", precision = 16, scale = 4)
    private BigDecimal LOTE_OPT;

    @Column(name = "MODELO", length = 1)
    private String MODELO;

    @Column(name = "Consumo Promedio", precision = 16, scale = 4)
    private BigDecimal CONSUMO_PROMEDIO;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Negocio", length = 50)
    private String NEGOCIO;

    @Size(max = 80)
    @Nationalized
    @Column(name = "LINEA", length = 80)
    private String LINEA;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SUBLINEA", length = 50)
    private String SUBLINEA;

    @Size(max = 60)
    @Nationalized
    @Column(name = "GRUPO", length = 60)
    private String GRUPO;

    @Column(name = "ca_cla-ni1_id")
    private Integer ca_cla_ni1_id;

    @Column(name = "ca_cla-ni2_id")
    private Integer ca_cla_ni2_id;

    @Column(name = "ca_cla-ni3_id")
    private Integer ca_cla_ni3_id;

    @Column(name = "ca_cla-ni4_id")
    private Integer ca_cla_ni4_id;

    @Column(name = "ca_cla-ni5_id")
    private Integer ca_cla_ni5_id;


}