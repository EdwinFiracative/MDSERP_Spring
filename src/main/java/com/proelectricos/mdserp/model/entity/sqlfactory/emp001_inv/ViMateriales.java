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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "vi_mat_op_2026", catalog = "EMP001_INV", schema = "dbo")
public class ViMateriales {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OP")
    private Integer OP;

    @Size(max = 250)
    @Column(name = "tdespacho", length = 250)
    private String tdespacho;

    @Size(max = 120)
    @Column(name = "nom_cliente", length = 120)
    private String nom_cliente;

    @Size(max = 100)
    @Column(name = "nom_op", length = 100)
    private String nom_op;

    @Column(name = "fecha_ini_op")
    private LocalDate fecha_ini_op;

    @Column(name = "FECHA_T")
    private LocalDate FECHA_T;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 6)
    @Column(name = "UBIC", length = 6)
    private String UBIC;

    @Size(max = 100)
    @Column(name = "nom_material", length = 100)
    private String nom_material;

    @Column(name = "cantidad_solicitada", precision = 38, scale = 4)
    private BigDecimal cantidad_solicitada;

    @NotNull
    @Column(name = "cantidad_comprada", nullable = false, precision = 38, scale = 4)
    private BigDecimal cantidad_comprada;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal EXIST;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String UD;

    @Column(name = "cantidad_entregada", precision = 38, scale = 4)
    private BigDecimal cantidad_entregada;

    @Column(name = "dif_cantidad", precision = 38, scale = 4)
    private BigDecimal dif_cantidad;

    @Column(name = "cos_est_unitario", precision = 16, scale = 4)
    private BigDecimal cos_est_unitario;

    @Column(name = "cos_real_unitario", precision = 38, scale = 6)
    private BigDecimal cos_real_unitario;

    @Column(name = "costo_solicitado", precision = 38, scale = 6)
    private BigDecimal costo_solicitado;

    @Column(name = "costo_entregado", precision = 38, scale = 4)
    private BigDecimal costo_entregado;

    @Column(name = "dif_costo", precision = 38, scale = 4)
    private BigDecimal dif_costo;

    @Size(max = 30)
    @Nationalized
    @Column(name = "\"ca_cla-ni1_nombre\"", length = 30)
    private String ca_cla_ni1_nombre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"ca_cla-ni2_nombre\"", length = 50)
    private String ca_cla_ni2_nombre;

    @Size(max = 80)
    @Nationalized
    @Column(name = "\"ca_cla-ni3_nombre\"", length = 80)
    private String ca_cla_ni3_nombre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", length = 50)
    private String ca_cla_ni4_nombre;

    @Size(max = 60)
    @Nationalized
    @Column(name = "\"ca_cla-ni5_nombre\"", length = 60)
    private String ca_cla_ni5_nombre;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal CANTP;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal CANTE;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Expr3", nullable = false, length = 50)
    private String Expr3;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "Expr1", nullable = false, length = 80)
    private String Expr1;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Expr2", nullable = false, length = 50)
    private String Expr2;


}
