package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "PEDIDOS", catalog = "EMP001_FACT", schema = "dbo")
public class Pedido {
    @Id
    @NotNull
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 15)
    @Column(name = "DESTINAT", length = 15)
    private String destinat;

    @Size(max = 250)
    @Column(name = "TDESPACHO", length = 250)
    private String tdespacho;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String num;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String cliente;

    @Column(name = "FECHA")
    private Instant fecha;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

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

    @Column(name = "DESP", precision = 14, scale = 4)
    private BigDecimal desp;

    @Column(name = "DESC3", precision = 8, scale = 4)
    private BigDecimal desc3;

    @Size(max = 4)
    @Column(name = "ZONA", length = 4)
    private String zona;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String vendedor;

    @Column(name = "ENTREGA")
    private Instant entrega;

    @Size(max = 50)
    @Column(name = "ORDEN", length = 50)
    private String orden;

    @Size(max = 6)
    @Column(name = "NCOD", length = 6)
    private String ncod;

    @Column(name = "FECHAN")
    private Instant fechan;

    @Size(max = 10)
    @Column(name = "SYS2015", length = 10)
    private String sys2015;

    @Lob
    @Column(name = "DETALLE")
    private String detalle;

    @Size(max = 30)
    @Column(name = "CONDIC", length = 30)
    private String condic;


}