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
@Table(name = "CO_PAGOS_2026", catalog = "EMP001_FACT", schema = "dbo")
public class CoPagosCar {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 4)
    @Column(name = "PREFIJO", length = 4)
    private String PREFIJO;

    @Column(name = "NUMERO")
    private Integer NUMERO;

    @Column(name = "FECFACT")
    private LocalDate FECFACT;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String CLIENTE;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String NIT;

    @Size(max = 120)
    @Column(name = "NOM", length = 120)
    private String NOM;

    @Size(max = 7)
    @Column(name = "PERIODO", length = 7)
    private String PERIODO;

    @Column(name = "VLR_FRA", precision = 16, scale = 4)
    private BigDecimal VLR_FRA;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Column(name = "NUM_FACT_PAGOS")
    private Integer NUM_FACT_PAGOS;

    @Size(max = 4)
    @Column(name = "CONC", length = 4)
    private String CONC;

    @Size(max = 6)
    @Column(name = "RECIBO", length = 6)
    private String RECIBO;

    @Column(name = "FECREC")
    private LocalDate FECREC;

    @Column(name = "VALOR", precision = 16, scale = 4)
    private BigDecimal VALOR;

    @Size(max = 2)
    @Column(name = "BANCO", length = 2)
    private String BANCO;

    @Size(max = 20)
    @Column(name = "ENTIDAD", length = 20)
    private String ENTIDAD;

    @Size(max = 120)
    @Column(name = "DETA", length = 120)
    private String DETA;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String VENDEDOR;

    @Size(max = 30)
    @Column(name = "EJECUTIVO", length = 30)
    private String EJECUTIVO;

    @Column(name = "VALORX", precision = 16, scale = 4)
    private BigDecimal VALORX;

    @Column(name = "\"COSTO ESTANDAR\"", precision = 38, scale = 8)
    private BigDecimal COSTO_ESTANDAR;

    @Column(name = "MARGEN", precision = 38, scale = 6)
    private BigDecimal MARGEN;

    @Size(max = 25)
    @Column(name = "CIU", length = 25)
    private String CIU;

    @Column(name = "VR_MCIA", precision = 16, scale = 4)
    private BigDecimal VR_MCIA;

    @Size(max = 50)
    @Column(name = "CONCEPTO", length = 50)
    private String CONCEPTO;

    @Column(name = "FDCTO")
    private LocalDate FDCTO;

    @Column(name = "DIAS_PAGO_FEC_FACTURA")
    private Integer DIAS_PAGO_FEC_FACTURA;

    @Column(name = "DIAS_PAGO_FEC_VCTO")
    private Integer DIAS_PAGO_FEC_VCTO;

    @Column(name = "\"AÑO_RECIBO\"")
    private Integer AÑO_RECIBO;

    @Size(max = 7)
    @Column(name = "PERIODO_RECIBO", length = 7)
    private String PERIODO_RECIBO;

    @Size(max = 5)
    @Column(name = "PEDIDO", length = 5)
    private String PEDIDO;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String NUM;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LINEAS", length = 50)
    private String LINEAS;

    @Size(max = 10)
    @NotNull
    @Column(name = "\"CUMPLIMIENTO PAGOS\"", nullable = false, length = 10)
    private String CUMPLIMIENTO_PAGOS;


}
