package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "VENDEDOR", catalog = "EMP001_FACT", schema = "dbo")
public class Vendedor {

    @Column(name = "Id", nullable = false)
    private Integer id;

    @Id
    @Size(max = 5)
    @Column(name = "COD", length = 5, unique = true)
    private String cod;

    @Size(max = 30)
    @Column(name = "NOM", length = 30)
    private String nom;

    @PostLoad
    private void trimStrings() {

        this.cod = trimAll(this.cod);
        this.nom = trimAll(this.nom);

    }

    private String trimAll(String value) {
        if (value == null) return null;
        // Removes all Unicode whitespace including NBSP)
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }


}