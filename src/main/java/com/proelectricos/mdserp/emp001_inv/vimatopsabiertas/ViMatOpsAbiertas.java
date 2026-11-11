package com.proelectricos.mdserp.emp001_inv.vimatopsabiertas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Materiales de las OPs abiertas del año (receta vs kardex).
 * No existe tabla ni vista en BD: las filas se obtienen únicamente con la consulta nativa
 * de ViMatOpsAbiertasRepository. El nombre de @Table es solo nominal (no se consulta).
 */
@Getter
@Entity
@Immutable
@Table(name = "vi_mat_ops_abiertas", catalog = "EMP001_INV", schema = "dbo")
public class ViMatOpsAbiertas {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "op")
    private Integer op;

    @Size(max = 20)
    @Column(name = "Ref", length = 20)
    private String ref;

    @Size(max = 100)
    @Column(name = "NombreProducto", length = 100)
    private String nombreProducto;

    @Size(max = 2)
    @Column(name = "ud", length = 2)
    private String ud;

    @Size(max = 2)
    @Column(name = "grup", length = 2)
    private String grup;

    @Column(name = "cstd", precision = 16, scale = 4)
    private BigDecimal cstd;

    @Size(max = 8)
    @Column(name = "tipo", length = 8)
    private String tipo;

    @Column(name = "cantp", precision = 14, scale = 4)
    private BigDecimal cantp;

    @Column(name = "cante", precision = 14, scale = 4)
    private BigDecimal cante;

    @Column(name = "FECHA_I")
    private LocalDate fechaI;

    @Column(name = "FECHA_T")
    private LocalDate fechaT;

    @Size(max = 1)
    @Column(name = "ESTADO", length = 1)
    private String estado;

    @Size(max = 20)
    @Column(name = "codMaterial", length = 20)
    private String codMaterial;

    @Size(max = 100)
    @Column(name = "NombreMaterial", length = 100)
    private String nombreMaterial;

    @Size(max = 1)
    @Column(name = "modelo", length = 1)
    private String modelo;

    @Column(name = "cantMaster", precision = 38, scale = 6)
    private BigDecimal cantMaster;

    @Column(name = "valorMaster", precision = 38, scale = 2)
    private BigDecimal valorMaster;

    @Column(name = "cantKardex", precision = 38, scale = 4)
    private BigDecimal cantKardex;

    @Column(name = "valorK", precision = 38, scale = 0)
    private BigDecimal valorK;

    @Size(max = 30)
    @Nationalized
    @Column(name = "Negocio", length = 30)
    private String negocio;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Clase", length = 50)
    private String clase;

    @Size(max = 80)
    @Nationalized
    @Column(name = "Linea", length = 80)
    private String linea;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Grupo", length = 50)
    private String grupo;

    @Size(max = 60)
    @Nationalized
    @Column(name = "Nivel5", length = 60)
    private String nivel5;
}
