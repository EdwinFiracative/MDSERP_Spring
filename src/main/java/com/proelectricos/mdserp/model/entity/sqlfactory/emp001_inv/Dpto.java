package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "DPTO", catalog = "EMP001_INV", schema = "dbo")
public class Dpto {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer ID;

    @Size(max = 4)
    @Column(name = "COD", length = 4)
    private String COD;

    @Size(max = 35)
    @Column(name = "NOM", length = 35)
    private String NOM;

    @Column(name = "CMO", precision = 16, scale = 4)
    private BigDecimal CMO;

    @Column(name = "CCF", precision = 16, scale = 4)
    private BigDecimal CCF;

    @Column(name = "SMO", precision = 16, scale = 4)
    private BigDecimal SMO;

    @Column(name = "SCF", precision = 16, scale = 4)
    private BigDecimal SCF;

    @Column(name = "ACTIVO", precision = 16, scale = 4)
    private BigDecimal ACTIVO;

    @Column(name = "MUERTO", precision = 16, scale = 4)
    private BigDecimal MUERTO;

    @Column(name = "ESTADO", precision = 2)
    private BigDecimal ESTADO;

    @Column(name = "EXTRAS", precision = 16, scale = 4)
    private BigDecimal EXTRAS;

    @Column(name = "RETRA", precision = 16, scale = 4)
    private BigDecimal RETRA;

    @Column(name = "ALMUE", precision = 16, scale = 4)
    private BigDecimal ALMUE;

    @Column(name = "PARADA", precision = 16, scale = 4)
    private BigDecimal PARADA;

    @Column(name = "POSIBLE", precision = 16, scale = 4)
    private BigDecimal POSIBLE;

    @Column(name = "NCONTROL", precision = 1)
    private BigDecimal NCONTROL;

    @Column(name = "INI_H")
    private Integer INI_H;

    @Column(name = "INI_M")
    private Integer INI_M;

    @Size(max = 15)
    @Column(name = "TELEFONO", length = 15)
    private String TELEFONO;

    @Column(name = "HORAMAQ", precision = 14, scale = 4)
    private BigDecimal HORAMAQ;

    @Column(name = "INACTIVA", precision = 2)
    private BigDecimal INACTIVA;

    @Column(name = "FINACTIVA")
    private LocalDate FINACTIVA;

    @Column(name = "PROAREA", precision = 2)
    private BigDecimal PROAREA;

    @Column(name = "CMONIIF", precision = 16, scale = 4)
    private BigDecimal CMONIIF;

    @Column(name = "CCFNIIF", precision = 16, scale = 4)
    private BigDecimal CCFNIIF;

    @Column(name = "PVENTADPTO", precision = 11)
    private BigDecimal PVENTADPTO;

    @Size(max = 2)
    @Column(name = "CLASIFICA", length = 2)
    private String CLASIFICA;

    @Column(name = "FACTOR", precision = 18, scale = 2)
    private BigDecimal FACTOR;

    @Column(name = "AJUSTECEN")
    private Integer AJUSTECEN;

    @Column(name = "JIKKOID")
    private Integer JIKKOID;

    @Column(name = "ACTIVOC")
    private Character ACTIVOC;

    @Size(max = 2)
    @Column(name = "BODDEFECTO", length = 2)
    private String BODDEFECTO;


}
