package com.proelectricos.mdserp.model.entity.sqlfactory;

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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "co_cartera_negocio", catalog = "EMP001_FACT", schema = "dbo")
public class CoCarteraNegocio {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 120)
    @Column(name = "NOMBRE", length = 120)
    private String nombre;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String cliente;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String nit;

    @Size(max = 7)
    @Column(name = "PEDIDO", length = 7)
    private String pedido;

    @Size(max = 17)
    @NotNull
    @Column(name = "CONCEPTO", nullable = false, length = 17)
    private String concepto;

    @Size(max = 4)
    @Column(name = "PREFIJO", length = 4)
    private String prefijo;

    @Column(name = "NUMERO")
    private Integer numero;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "VENCE")
    private LocalDate vence;

    @Column(name = "\"DIAS VENCIDO\"")
    private Integer diasVencido;

    @Column(name = "\"DIAS TOTALES\"")
    private Integer diasTotales;

    @Column(name = "\"VALOR TOTAL\"", precision = 16, scale = 4)
    private BigDecimal valorTotal;

    @Size(max = 50)
    @Nationalized
    @Column(name = "NEGOCIO", length = 50)
    private String negocio;

    @Column(name = "\"PORCENTAJE NEGOCIO\"", precision = 38, scale = 6)
    private BigDecimal porcentajeNegocio;

    @Column(name = "\"VALOR PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal valorPonderado;

    @Column(name = "\"ABONO TOTAL\"", precision = 16, scale = 4)
    private BigDecimal abonoTotal;

    @Column(name = "\"ABONO PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal abonoPonderado;

    @Column(name = "SALDO", precision = 17, scale = 4)
    private BigDecimal saldo;

    @Column(name = "\"SALDO PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal saldoPonderado;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String ciu;

    @Size(max = 30)
    @Column(name = "VENDEDOR", length = 30)
    private String vendedor;


}