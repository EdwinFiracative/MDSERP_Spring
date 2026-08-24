package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Immutable
@Table(name = "vi_time_mast_semie_op",   catalog = "EMP001_INV", schema = "dbo")
public class ViTimeMastSemieOp {

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

    @Size(max = 20)
    @Column(name = "CODMAST", length = 20)
    private String codmast;

    @Size(max = 100)
    @Column(name = "\"NOM MASTER\"", length = 100)
    private String nomMaster;

    @Size(max = 4)
    @Column(name = "CECO", length = 4)
    private String ceco;

    @Column(name = "HORAS", precision = 14, scale = 7)
    private BigDecimal horas;


}