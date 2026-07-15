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
import java.time.Instant;

@Getter
@Entity
@Immutable
@Table(name = "VI_OP_FABRICUR" ,  catalog = "EMP001_INV", schema = "dbo")
public class ViOpFabricur {
    @Id
    @Column(name = "OP")
    private Integer op;

    @Column(name = "MODELO")
    private Character modelo;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String nom;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String ud;

    @Size(max = 2)
    @Column(name = "GRUP", length = 2)
    private String grup;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal cantp;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal cante;

    @Column(name = "FECHA_I")
    private Instant fechaI;

    @Column(name = "FECHA_T")
    private Instant fechaT;

    @Column(name = "ESTADO")
    private Character estado;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", nullable = false, length = 50)
    private String caClaNi4Nombre;

    @Size(max = 20)
    @Column(name = "codmp", length = 20)
    private String codmp;

    @Size(max = 100)
    @Column(name = "\"NOM MP\"", length = 100)
    private String nomMp;

    @Size(max = 2)
    @Column(name = "\"UD MP\"", length = 2)
    private String udMp;

    @Size(max = 2)
    @Column(name = "\"GRP MP\"", length = 2)
    private String grpMp;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal cant;

    @Size(max = 20)
    @Column(name = "CodK", length = 20)
    private String codK;

    @Column(name = "SumCantK", precision = 38, scale = 4)
    private BigDecimal sumCantK;


}
