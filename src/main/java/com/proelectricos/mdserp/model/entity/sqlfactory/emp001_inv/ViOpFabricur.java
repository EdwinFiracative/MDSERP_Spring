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
@Table(name = "VI_OP_FABRICUR" ,  catalog = "EMP001_INV", schema = "dbo")
public class ViOpFabricur {
    @Id
    @Column(name = "OP")
    private Integer OP;

    @Column(name = "MODELO")
    private Character MODELO;

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

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal CANTP;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal CANTE;

    @Column(name = "FECHA_I")
    private LocalDate FECHA_I;

    @Column(name = "FECHA_T")
    private LocalDate FECHA_T;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", nullable = false, length = 50)
    private String ca_cla_ni4_nombre;

    @Size(max = 20)
    @Column(name = "codmp", length = 20)
    private String codmp;

    @Size(max = 100)
    @Column(name = "\"NOM MP\"", length = 100)
    private String NOM_MP;

    @Size(max = 2)
    @Column(name = "\"UD MP\"", length = 2)
    private String UD_MP;

    @Size(max = 2)
    @Column(name = "\"GRP MP\"", length = 2)
    private String GRP_MP;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal CANT;

    @Size(max = 20)
    @Column(name = "CodK", length = 20)
    private String CodK;

    @Column(name = "SumCantK", precision = 38, scale = 4)
    private BigDecimal SumCantK;


}
