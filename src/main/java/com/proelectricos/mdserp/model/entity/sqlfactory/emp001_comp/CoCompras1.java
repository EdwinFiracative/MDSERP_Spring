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
@Table(name = "CO_Compras_1_2026", catalog = "EMP001_COMP", schema = "dbo")
public class CoCompras1 {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OC")
    private Integer OC;

    @Size(max = 15)
    @Column(name = "CPROV", length = 15)
    private String CPROV;

    @Size(max = 60)
    @Column(name = "NOMBRE", length = 60)
    private String NOMBRE;

    @Size(max = 4000)
    @Nationalized
    @Column(name = "\"FECHA CREACION\"", length = 4000)
    private String FECHA_CREACION;

    @Size(max = 4000)
    @Nationalized
    @Column(name = "\"FECHA PLANEADA\"", length = 4000)
    private String FECHA_PLANEADA;

    @Column(name = "COMO")
    private Integer COMO;

    @Column(name = "VAN")
    private Integer VAN;

    @Column(name = "ESTADO")
    private Character ESTADO;

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

    @Column(name = "CANTP", precision = 29, scale = 8)
    private BigDecimal CANTP;

    @Column(name = "CANTE", precision = 29, scale = 8)
    private BigDecimal CANTE;

    @Column(name = "ESTAD")
    private Character ESTAD;

    @NotNull
    @Column(name = "CANT_K", nullable = false, precision = 38, scale = 4)
    private BigDecimal CANT_K;

    @Size(max = 30)
    @Column(name = "PNIT", length = 30)
    private String PNIT;

    @Column(name = "CANT_PEND", precision = 38, scale = 4)
    private BigDecimal CANT_PEND;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "NEGOCIO", nullable = false, length = 30)
    private String NEGOCIO;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "LINEA", nullable = false, length = 50)
    private String LINEA;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "CLASE", nullable = false, length = 80)
    private String CLASE;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "GRUPO", nullable = false, length = 50)
    private String GRUPO;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "NIVEL5", nullable = false, length = 60)
    private String NIVEL5;


}
