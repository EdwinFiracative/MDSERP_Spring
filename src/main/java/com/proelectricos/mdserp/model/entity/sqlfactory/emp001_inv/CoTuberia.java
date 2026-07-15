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

@Getter
@Entity
@Immutable
@Table(name = "CO_TUBERIA" ,  catalog = "EMP001_INV", schema = "dbo")
public class CoTuberia {
    @Id
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

    @Size(max = 2)
    @Column(name = "ALMA", length = 2)
    private String alma;

    @Size(max = 30)
    @Column(name = "BODEGA", length = 30)
    private String bodega;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal exist;

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
