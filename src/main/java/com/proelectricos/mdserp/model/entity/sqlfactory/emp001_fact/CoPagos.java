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
@Table(name = "CO_PAGOS" ,  catalog = "EMP001_FACT", schema = "dbo")
public class CoPagos {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 4)
    @Column(name = "PREFIJO", length = 4)
    private String prefijo;

    @Column(name = "NUMERO")
    private Integer numero;

    @Column(name = "FECFACT")
    private LocalDate fecfact;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String cliente;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String nit;

    @Size(max = 120)
    @Column(name = "NOM", length = 120)
    private String nom;

    @Size(max = 25)
    @NotNull
    @Column(name = "PERIODO", nullable = false, length = 25)
    private String periodo;

    @Column(name = "VLR_FRA", precision = 16, scale = 4)
    private BigDecimal vlrFra;

    @Column(name = "ESTADO")
    private Character estado;

    @Column(name = "NUM_FACT_PAGOS")
    private Integer numFactPagos;

    @Size(max = 4)
    @Column(name = "CONC", length = 4)
    private String conc;

    @Size(max = 6)
    @Column(name = "RECIBO", length = 6)
    private String recibo;

    @Column(name = "FECREC")
    private LocalDate fecrec;

    @Column(name = "VALOR", precision = 16, scale = 4)
    private BigDecimal valor;

    @Size(max = 2)
    @Column(name = "BANCO", length = 2)
    private String banco;

    @Size(max = 20)
    @Column(name = "ENTIDAD", length = 20)
    private String entidad;

    @Size(max = 120)
    @Column(name = "DETA", length = 120)
    private String deta;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String vendedor;

    @Size(max = 30)
    @Column(name = "EJECUTIVO", length = 30)
    private String ejecutivo;

    @Column(name = "VALORX", precision = 16, scale = 4)
    private BigDecimal valorx;

    @Column(name = "\"COSTO ESTANDAR\"", precision = 38, scale = 8)
    private BigDecimal costoEstandar;

    @Column(name = "MARGEN", precision = 38, scale = 6)
    private BigDecimal margen;

    @Size(max = 25)
    @Column(name = "CIU", length = 25)
    private String ciu;

    @Column(name = "VR_MCIA", precision = 16, scale = 4)
    private BigDecimal vrMcia;

    @Size(max = 50)
    @Column(name = "CONCEPTO", length = 50)
    private String concepto;

    @Column(name = "FDCTO")
    private LocalDate fdcto;

    @Column(name = "DIAS_PAGO_FEC_FACTURA")
    private Integer diasPagoFecFactura;

    @Column(name = "DIAS_PAGO_FEC_VCTO")
    private Integer diasPagoFecVcto;

    @Column(name = "\"AÑO_RECIBO\"")
    private Integer añoRecibo;

    @Size(max = 25)
    @NotNull
    @Column(name = "PERIODO_RECIBO", nullable = false, length = 25)
    private String periodoRecibo;

    @Size(max = 5)
    @Column(name = "PEDIDO", length = 5)
    private String pedido;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String num;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LINEAS", length = 50)
    private String lineas;

    @Size(max = 10)
    @NotNull
    @Column(name = "\"CUMPLIMIENTO PAGOS\"", nullable = false, length = 10)
    private String cumplimientoPagos;


}
