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
@Table(name = "co_op_mp_entr_2026", catalog = "EMP001_INV", schema = "dbo")
public class Materiales {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OP")
    private Integer OP;

    @Size(max = 20)
    @Column(name = "CODOP", length = 20)
    private String CODOP;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal CANTP;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal CANTE;

    @Column(name = "FECHA_I")
    private Instant FECHA_I;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Size(max = 7)
    @Column(name = "Perido", length = 7)
    private String Perido;

    @Size(max = 20)
    @Column(name = "CODMP", length = 20)
    private String CODMP;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String NOM;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal CANT;

    @Column(name = "CMASTER", precision = 14, scale = 4)
    private BigDecimal CMASTER;

    @NotNull
    @Column(name = "CantidaK", nullable = false, precision = 38, scale = 4)
    private BigDecimal CantidaK;

    @NotNull
    @Column(name = "VMPK", nullable = false, precision = 38)
    private BigDecimal VMPK;

    @Column(name = "CANTDISP", precision = 38, scale = 4)
    private BigDecimal CANTDISP;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 30)
    private String Negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 50)
    private String Clase;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "Linea", nullable = false, length = 80)
    private String Linea;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Grupo", nullable = false, length = 50)
    private String Grupo;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "Nivel5", nullable = false, length = 60)
    private String Nivel5;


}
