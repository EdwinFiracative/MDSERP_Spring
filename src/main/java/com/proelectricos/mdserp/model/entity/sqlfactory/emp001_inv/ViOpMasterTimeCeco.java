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
@Table(name = "vi_op_master_time_ceco",   catalog = "EMP001_INV", schema = "dbo")
public class ViOpMasterTimeCeco {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OP")
    private Integer op;

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

    @Size(max = 4)
    @Column(name = "CECO", length = 4)
    private String ceco;

    @Size(max = 35)
    @Column(name = "NOMCECO", length = 35)
    private String nomceco;

    @Column(name = "\"CANTIDAD HRS\"", precision = 18, scale = 7)
    private BigDecimal cantidadHrs;

    @Column(name = "\"VLR CMO\"", precision = 35, scale = 11)
    private BigDecimal vlrCmo;

    @Column(name = "\"VLR CCF\"", precision = 35, scale = 11)
    private BigDecimal vlrCcf;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 30)
    private String negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Linea", nullable = false, length = 50)
    private String linea;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 80)
    private String clase;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Grupo", nullable = false, length = 50)
    private String grupo;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "Nivel5", nullable = false, length = 60)
    private String nivel5;


}