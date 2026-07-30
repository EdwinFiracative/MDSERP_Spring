package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "tabErpPedidoNotes", catalog = "EMP001_FACT", schema = "dbo")
public class ViewErpPedidoNote {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @ManyToOne
    @JoinColumn(name = "NUM")
    private ViewErpPedidoHeader num;

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
