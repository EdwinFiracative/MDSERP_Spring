package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "MAESTRO", catalog = "EMP001_INV", schema = "dbo")
public class Referencia {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 60)
    @Column(name = "CODCLIE", length = 60)
    private String codclie;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String nom;

    @Size(max = 2)
    @Column(name = "GRUP", length = 2)
    private String grup;

    @Size(max = 100)
    @Column(name = "NOM1", length = 100)
    private String nom1;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String ud;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal exist;

    @Column(name = "SMIN", precision = 14, scale = 4)
    private BigDecimal smin;

    @Column(name = "SMAX", precision = 14, scale = 4)
    private BigDecimal smax;

    @Column(name = "CMP", precision = 14)
    private BigDecimal cmp;

    @Column(name = "CMR", precision = 16, scale = 4)
    private BigDecimal cmr;

    @Column(name = "IVA", precision = 8, scale = 4)
    private BigDecimal iva;

    @Column(name = "GRUPO")
    private Character grupo;

    @Column(name = "CSTD", precision = 16, scale = 4)
    private BigDecimal cstd;

    @Column(name = "ICA", precision = 8, scale = 4)
    private BigDecimal ica;

    @PostLoad
    private void trimStrings() {
        this.codclie = trimAll(this.codclie);
        this.cod = trimAll(this.cod);
        this.nom = trimAll(this.nom);
        this.nom1 = trimAll(this.nom1);
        this.ud = trimAll(this.ud);
    }

    private String trimAll(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }
}