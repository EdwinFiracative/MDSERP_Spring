package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "CLIENTES", catalog = "EMP001_FACT", schema = "dbo")
public class Cliente {

    @Column(name = "Id", nullable = false)
    private Integer id;

    @Id
    @Size(max = 15)
    @Column(name = "COD", length = 15)
    private String cod;

    @Size(max = 120)
    @Column(name = "NOM", length = 120)
    private String nom;

    @Size(max = 200)
    @Column(name = "DIR", length = 200)
    private String dir;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String ciu;

    @Size(max = 30)
    @Column(name = "DEPTO", length = 30)
    private String depto;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String nit;

    @Size(max = 4)
    @Column(name = "DIST", length = 4)
    private String dist;

    @Size(max = 30)
    @Column(name = "CONDIC", length = 30)
    private String condic;

    @Column(name = "CUPO", precision = 16, scale = 4)
    private BigDecimal cupo;

    @Size(max = 3)
    @Column(name = "ZONA", length = 3)
    private String zona;

    @Size(max = 6)
    @Column(name = "NCOD", length = 6)
    private String ncod;

    @PostLoad
    private void trimStrings() {

        this.nom = trimAll(this.nom);
        this.dir = trimAll(this.dir);
        this.ciu = trimAll(this.ciu);
        this.depto = trimAll(this.depto);
        this.nit = trimAll(this.nit);
        this.dist = trimAll(this.dist);
        this.condic = trimAll(this.condic);
        this.zona = trimAll(this.zona);
    }

    private String trimAll(String value) {
        if (value == null) return null;
        // Removes all Unicode whitespace including NBSP)
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }

}