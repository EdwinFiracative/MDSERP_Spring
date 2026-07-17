package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "vi_mat_ope_op_2026", catalog = "EMP001_INV", schema = "dbo")
public class ViMaterialesOP {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 6)
    @Column(name = "UBIC", length = 6)
    private String UBIC;

    @Column(name = "cantidad_a_comprar", precision = 38, scale = 4)
    private BigDecimal cantidad_a_comprar;

    @Column(name = "cos_est_unitario", precision = 16, scale = 4)
    private BigDecimal cos_est_unitario;

    @Column(name = "OP")
    private Integer OP;

    // Esta columna en la vista puede contener valores no convertibles a fecha en SQL Server.
    // Se deja fuera del mapeo JPA para evitar que falle la consulta completa.
    @Transient
    private LocalDate Mat_Elec_Plan;

    @Size(max = 7)
    @Column(name = "pedido", length = 7)
    private String pedido;

    @Column(name = "CMR", precision = 16, scale = 4)
    private BigDecimal CMR;

    @Column(name = "cantidad_solicitada", precision = 38, scale = 4)
    private BigDecimal cantidad_solicitada;

    @Column(name = "cantidad_entregada", precision = 38, scale = 4)
    private BigDecimal cantidad_entregada;

    @Column(name = "cantidad_pendiente", precision = 38, scale = 4)
    private BigDecimal cantidad_pendiente;

    @NotNull
    @Column(name = "cantidad_comprada", nullable = false, precision = 38, scale = 4)
    private BigDecimal cantidad_comprada;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal EXIST;

    @Size(max = 100)
    @Column(name = "nom_material", length = 100)
    private String nom_material;

    @Size(max = 100)
    @Column(name = "nom_op", length = 100)
    private String nom_op;

    @Size(max = 250)
    @NotNull
    @Column(name = "proyecto", nullable = false, length = 250)
    private String proyecto;

    @Size(max = 120)
    @NotNull
    @Column(name = "cliente", nullable = false, length = 120)
    private String cliente;

    @Column(name = "fecha_ini_op")
    private LocalDate fecha_ini_op;

    @Column(name = "FECHA_T")
    private LocalDate FECHA_T;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal CANTP;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal CANTE;

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


}
