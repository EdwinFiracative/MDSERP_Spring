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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "vi_ord_abiertas_2026", catalog = "EMP001_COMP", schema = "dbo")
public class ViOrdAbiertas {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 1)
    @NotNull
    @Column(name = "TIPO_OC", nullable = false, length = 1)
    private String tipoOc;

    @Column(name = "FAPER")
    private LocalDate faper;

    @Column(name = "OC")
    private Integer oc;

    @Size(max = 60)
    @Column(name = "vca_rco_proveedor", length = 60)
    private String vcaRcoProveedor;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String nom;

    @Column(name = "FPROM")
    private LocalDate fprom;

    @Column(name = "OP")
    private String op;

    @Column(name = "pendiente", precision = 15, scale = 4)
    private BigDecimal pendiente;

    @Column(name = "ESTADO")
    private Character estado;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal cantp;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal cante;

    @Column(name = "\"COSTO ESTANDAR\"", precision = 16, scale = 4)
    private BigDecimal costoEstandar;

    @Column(name = "cost_unitario", precision = 16, scale = 4)
    private BigDecimal costUnitario;

    @Column(name = "venctto")
    private Integer venctto;

    @Column(name = "cos_pedido", precision = 31, scale = 8)
    private BigDecimal cosPedido;

    @Column(name = "cos_entregado", precision = 31, scale = 8)
    private BigDecimal cosEntregado;

    @Column(name = "cos_pendiente", precision = 32, scale = 8)
    private BigDecimal cosPendiente;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String ud;

    @Size(max = 6)
    @Column(name = "AUTOFIN", length = 6)
    private String autofin;

    @Size(max = 25)
    @Column(name = "USR", length = 25)
    private String usr;

    @Column(name = "FAPROFIN")
    private LocalDate faprofin;

    @Size(max = 30)
    @Nationalized
    @Column(name = "\"ca_cla-ni1_nombre\"", length = 30)
    private String caClaNi1Nombre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"ca_cla-ni2_nombre\"", length = 50)
    private String caClaNi2Nombre;

    @Size(max = 80)
    @Nationalized
    @Column(name = "\"ca_cla-ni3_nombre\"", length = 80)
    private String caClaNi3Nombre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", length = 50)
    private String caClaNi4Nombre;

    @Size(max = 60)
    @Nationalized
    @Column(name = "\"ca_cla-ni5_nombre\"", length = 60)
    private String caClaNi5Nombre;


}