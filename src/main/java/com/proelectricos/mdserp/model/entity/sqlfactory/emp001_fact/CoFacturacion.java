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
import java.time.Instant;

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
    private String prefijo;

    @Size(max = 10)
    @Column(name = "PEDID", length = 10)
    private String pedid;

    @Column(name = "fftra")
    private Instant fftra;

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
    private String codCliente;

    @Size(max = 120)
    @Column(name = "nom_cliente", length = 120)
    private String nomCliente;

    @Column(name = "ESTADO")
    private Character estado;

    @Size(max = 4)
    @Column(name = "DIST", length = 4)
    private String dist;

    @Size(max = 40)
    @Column(name = "nom_dist", length = 40)
    private String nomDist;

    @Size(max = 3)
    @Column(name = "ZONA", length = 3)
    private String zona;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String ciu;

    @Size(max = 5)
    @Column(name = "cod_vdor", length = 5)
    private String codVdor;

    @Size(max = 30)
    @Column(name = "nom_vdor", length = 30)
    private String nomVdor;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Size(max = 100)
    @Column(name = "NOM1", length = 100)
    private String nom1;

    @Column(name = "cant", precision = 14)
    private BigDecimal cant;

    @Column(name = "vlr_unit", precision = 16)
    private BigDecimal vlrUnit;

    @Column(name = "subtotal_linea", precision = 31, scale = 8)
    private BigDecimal subtotalLinea;

    @NotNull
    @Column(name = "cant_dev", nullable = false, precision = 38, scale = 4)
    private BigDecimal cantDev;

    @NotNull
    @Column(name = "valor_devolucion", nullable = false, precision = 38, scale = 8)
    private BigDecimal valorDevolucion;

    @NotNull
    @Column(name = "costo_kardex", nullable = false, precision = 38)
    private BigDecimal costoKardex;

    @NotNull
    @Column(name = "costo_dev_kardex", nullable = false, precision = 38)
    private BigDecimal costoDevKardex;

    @Column(name = "cst_stand", precision = 16)
    private BigDecimal cstStand;

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
    private BigDecimal costoTotalFinal;

    @Column(name = "utilidad", precision = 38)
    private BigDecimal utilidad;

    @Column(name = "ca_margen", precision = 38, scale = 6)
    private BigDecimal caMargen;


}
