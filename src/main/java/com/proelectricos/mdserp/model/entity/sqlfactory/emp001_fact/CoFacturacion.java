package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "CO_FACTURACION_2026",  catalog = "EMP001_FACT", schema = "dbo")
public class CoFacturacion {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "factura")
    private Integer factura;

    @Size(max = 4)
    @Column(name = "PREFIJO", length = 4)
    private String PREFIJO;

    @Size(max = 10)
    @Column(name = "PEDID", length = 10)
    private String PEDID;

    @Column(name = "fftra")
    private LocalDate fftra;

    @Size(max = 7)
    @Column(name = "periodo", length = 7)
    private String periodo;

    @Column(name = "\"año\"")
    private Integer año;

    @Size(max = 30)
    @Nationalized
    @Column(name = "mes", length = 30)
    private String mes;

    @Size(max = 15)
    @Column(name = "cod_cliente", length = 15)
    private String cod_cliente;

    @Size(max = 120)
    @Column(name = "nom_cliente", length = 120)
    private String nom_cliente;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Size(max = 4)
    @Column(name = "DIST", length = 4)
    private String DIST;

    @Size(max = 40)
    @Column(name = "nom_dist", length = 40)
    private String nom_dist;

    @Size(max = 3)
    @Column(name = "ZONA", length = 3)
    private String ZONA;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String CIU;

    @Size(max = 5)
    @Column(name = "cod_vdor", length = 5)
    private String cod_vdor;

    @Size(max = 30)
    @Column(name = "nom_vdor", length = 30)
    private String nom_vdor;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Size(max = 100)
    @Column(name = "NOM1", length = 100)
    private String NOM1;

    @Column(name = "cant", precision = 14)
    private BigDecimal cant;

    @Column(name = "vlr_unit", precision = 16)
    private BigDecimal vlr_unit;

    @Column(name = "subtotal_linea", precision = 31, scale = 8)
    private BigDecimal subtotal_linea;

    @NotNull
    @Column(name = "cant_dev", nullable = false, precision = 38, scale = 4)
    private BigDecimal cant_dev;

    @NotNull
    @Column(name = "valor_devolucion", nullable = false, precision = 38, scale = 8)
    private BigDecimal valor_devolucion;

    @NotNull
    @Column(name = "costo_kardex", nullable = false, precision = 38)
    private BigDecimal costo_kardex;

    @NotNull
    @Column(name = "costo_dev_kardex", nullable = false, precision = 38)
    private BigDecimal costo_dev_kardex;

    @Column(name = "cst_stand", precision = 16)
    private BigDecimal cst_stand;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "negocio", nullable = false, length = 30)
    private String negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "clase", nullable = false, length = 50)
    private String clase;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "linea", nullable = false, length = 80)
    private String linea;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "grupo", nullable = false, length = 50)
    private String grupo;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "nivel", nullable = false, length = 60)
    private String nivel;

    @Column(name = "total", precision = 31)
    private BigDecimal total;

    @Column(name = "ventaneta", precision = 38)
    private BigDecimal ventaneta;

    @Column(name = "costo_total_final", precision = 38)
    private BigDecimal costo_total_final;

    @Column(name = "utilidad", precision = 38)
    private BigDecimal utilidad;

    @Column(name = "ca_margen", precision = 38, scale = 6)
    private BigDecimal ca_margen;


}
