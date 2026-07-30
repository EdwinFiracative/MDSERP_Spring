package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Referencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "viewErpPedidoReferences", catalog = "EMP001_FACT", schema = "dbo")
public class ViewErpPedidoReference {
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
    @ManyToOne
    @JoinColumn(name = "COD", referencedColumnName = "COD")
    private Referencia cod;

    @Size(max = 200)
    @Column(name = "NOM", length = 200)
    private String nom;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String ud;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal cant;

    @Column(name = "COSTO", precision = 16, scale = 4)
    private BigDecimal costo;

    @Column(name = "PEND", precision = 14, scale = 4)
    private BigDecimal pend;

    @Column(name = "ESTADO")
    private Character estado;

    @Column(name = "NETO", precision = 16, scale = 4)
    private BigDecimal neto;

    @Column(name = "ENTREGA")
    private Instant entrega;

    @NotNull
    @Column(name = "factoryId", nullable = false)
    private Integer factoryId;

    @PostLoad
    private void trimStrings() {
        this.nom = trimAll(this.nom);
        this.ud = trimAll(this.ud);
    }

    private String trimAll(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }

}
