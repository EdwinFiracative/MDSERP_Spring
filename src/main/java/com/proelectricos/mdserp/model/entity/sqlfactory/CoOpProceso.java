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

import java.time.LocalDate;



@Getter
@Entity
@Immutable
@Table(name = "co_op_proceso", catalog = "EMP001_INV", schema = "dbo")

public class CoOpProceso {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OP")
    private Integer op;

    @Column(name = "FECHA_I")
    private LocalDate fechaI;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String nom;

    @Size(max = 2)
    @Column(name = "GRUP", length = 2)
    private String grup;

    @Size(max = 40)
    @Column(name = "nom_grup", length = 40)
    private String nomGrup;

    @Size(max = 40)
    @Column(name = "NOTA", length = 40)
    private String nota;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal cantp;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal cante;

    @Column(name = "saldo", precision = 15, scale = 4)
    private BigDecimal saldo;

    @Column(name = "FECHA_T")
    private LocalDate fechaT;

    @Column(name = "costo_apl", precision = 17)
    private BigDecimal costoApl;

    @Column(name = "costo_liq", precision = 17)
    private BigDecimal costoLiq;

    @Column(name = "costo_proc", precision = 18)
    private BigDecimal costoProc;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "negocio", nullable = false, length = 50)
    private String negocio;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "linea", nullable = false, length = 80)
    private String linea;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "sub_linea", nullable = false, length = 50)
    private String subLinea;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "grupo", nullable = false, length = 60)
    private String grupo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "suma_decac_cant", precision = 38, scale = 6)
    private BigDecimal sumaDecacCant;

    @Column(name = "cant_uni", precision = 38, scale = 10)
    private Integer cantUni;

    @Column(name = "sum_cos_unit", precision = 38, scale = 6)
    private BigDecimal sumCosUnit;

    @Column(name = "VENCE")
    private LocalDate vence;


}