package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "CO_CATALOGO_PROD" ,  catalog = "EMP001_INV", schema = "dbo")
public class CoCatalogoProd {
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

    @Size(max = 40)
    @Column(name = "\"NOM GRUPO\"", length = 40)
    private String nomGrupo;

    @Size(max = 6)
    @Column(name = "UBIC", length = 6)
    private String ubic;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal exist;

    @Column(name = "CSTD", precision = 16, scale = 4)
    private BigDecimal cstd;

    @Column(name = "ACTIVO")
    private Character activo;

    @Size(max = 30)
    @Nationalized
    @Column(name = "Negocio", length = 30)
    private String negocio;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Linea", length = 50)
    private String linea;

    @Size(max = 80)
    @Nationalized
    @Column(name = "Clase", length = 80)
    private String clase;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Grupo", length = 50)
    private String grupo;

    @Size(max = 60)
    @Nationalized
    @Column(name = "Nivel5", length = 60)
    private String nivel5;


}
