package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_comp;

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
@Table(name = "vi_mrp", catalog = "EMP001_COMP", schema = "dbo")
public class ViMrp {

    @Id
    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String NOM;

    @NotNull
    @Column(name = "inv_existencia", nullable = false, precision = 38, scale = 4)
    private BigDecimal inv_existencia;

    @Column(name = "afi_compras", precision = 38, scale = 4)
    private BigDecimal afi_compras;

    @Column(name = "SMIN", precision = 14, scale = 4)
    private BigDecimal SMIN;

    @NotNull
    @Column(name = "solicitud_op", nullable = false, precision = 38, scale = 4)
    private BigDecimal solicitud_op;

    @Column(name = "comprar_unid_almacen", precision = 38, scale = 4)
    private BigDecimal comprar_unid_almacen;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String UD;

    @Column(name = "comprar_unid_compras", precision = 38, scale = 4)
    private BigDecimal comprar_unid_compras;

    @Column(name = "stock_max_uni_compras", precision = 33, scale = 19)
    private BigDecimal stock_max_uni_compras;

    @Size(max = 2)
    @Column(name = "UDA", length = 2)
    private String UDA;

    @Column(name = "CSTD", precision = 16, scale = 4)
    private BigDecimal CSTD;

    @Column(name = "costo_compra", precision = 38, scale = 6)
    private BigDecimal costo_compra;

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