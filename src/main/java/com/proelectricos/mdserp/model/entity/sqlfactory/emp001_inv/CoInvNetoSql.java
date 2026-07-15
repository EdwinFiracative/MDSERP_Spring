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
@Table(name = "CO_INV_NETO_SQL",   catalog = "EMP001_INV", schema = "dbo")
public class CoInvNetoSql {

    @Id
    @Column(name = "id")
    private Long id;

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

    @Column(name = "BOD31", precision = 38, scale = 4)
    private BigDecimal BOD31;

    @Column(name = "BOD32", precision = 38, scale = 4)
    private BigDecimal BOD32;

    @Column(name = "BOD20", precision = 38, scale = 4)
    private BigDecimal BOD20;

    @Column(name = "BOD39", precision = 38, scale = 4)
    private BigDecimal BOD39;

    @Column(name = "SMAX", precision = 14, scale = 4)
    private BigDecimal SMAX;

    @Column(name = "SMIN", precision = 14, scale = 4)
    private BigDecimal SMIN;

    @Column(name = "UNID_OP", precision = 38, scale = 4)
    private BigDecimal UNID_OP;

    @Column(name = "LOTE_OPT", precision = 16, scale = 4)
    private BigDecimal LOTE_OPT;

    @Column(name = "MODELO")
    private Character MODELO;

    @Column(name = "\"Consumo Promedio\"", precision = 16, scale = 4)
    private BigDecimal Consumo_Promedio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "NEGOCIO", nullable = false, length = 50)
    private String NEGOCIO;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "LINEA", nullable = false, length = 80)
    private String LINEA;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "SUBLINEA", nullable = false, length = 50)
    private String SUBLINEA;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "GRUPO", nullable = false, length = 60)
    private String GRUPO;

    @NotNull
    @Column(name = "\"ca_cla-ni1_id\"", nullable = false)
    private Integer ca_cla_ni1_id;

    @NotNull
    @Column(name = "\"ca_cla-ni2_id\"", nullable = false)
    private Integer ca_cla_ni2_id;

    @NotNull
    @Column(name = "\"ca_cla-ni3_id\"", nullable = false)
    private Integer ca_cla_ni3_id;

    @NotNull
    @Column(name = "\"ca_cla-ni4_id\"", nullable = false)
    private Integer ca_cla_ni4_id;

    @NotNull
    @Column(name = "\"ca_cla-ni5_id\"", nullable = false)
    private Integer ca_cla_ni5_id;


}
