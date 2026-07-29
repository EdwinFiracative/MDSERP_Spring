package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "viewErpPedidoNotes")
public class ViewErpPedidoNote {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String num;

    @Column(name = "POS")
    private Long pos;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 200)
    @Column(name = "NOM", length = 200)
    private String nom;

    @NotNull
    @Column(name = "factoryId", nullable = false)
    private Integer factoryId;

    @PostLoad
    private void trimStrings() {
        this.num = trimAll(this.num);
        this.cod = trimAll(this.cod);
        this.nom = trimAll(this.nom);
    }

    private String trimAll(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }

}
