package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.Instant;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "viewErpPedidoHeader", catalog = "EMP001_FACT", schema = "dbo")
public class ViewErpPedidoHeader {
    @Id
    @Size(max = 7)
    @Column(name = "num", length = 7)
    private String num;

    @Size(max = 15)
    @Column(name = "DESTINAT", length = 15)
    private String destinat;

    @Size(max = 250)
    @Column(name = "TDESPACHO", length = 250)
    private String tdespacho;

    @Size(max = 15)
    @Column(name = "cliente", length = 15)
    private String cliente;

    @Column(name = "fecha")
    private Instant fecha;

    @Size(max = 5)
    @Column(name = "vendedor", length = 5)
    private String vendedor;

    @Size(max = 50)
    @Column(name = "orden", length = 50)
    private String orden;

    @Size(max = 6)
    @Column(name = "ncod", length = 6)
    private String ncod;

    @Lob
    @Column(name = "detalle")
    private String detalle;

    @Size(max = 30)
    @Column(name = "condic", length = 30)
    private String condic;

    @NotNull
    @Column(name = "factoryId", nullable = false)
    private Integer factoryId;

    @OneToMany(mappedBy = "num", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ViewErpPedidoReference> references;

    @OneToMany(mappedBy = "num", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ViewErpPedidoNote> notes;

    @PostLoad
    private void trimStrings() {
        this.destinat = trimAll(this.destinat);
        this.tdespacho = trimAll(this.tdespacho);
        this.cliente = trimAll(this.cliente);
        this.vendedor = trimAll(this.vendedor);
        this.orden = trimAll(this.orden);
        this.ncod = trimAll(this.ncod);
        this.detalle = trimAll(this.detalle);
        this.condic = trimAll(this.condic);
    }

    private String trimAll(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("^[\\s\\u00A0]+|[\\s\\u00A0]+$", "");
    }

}
