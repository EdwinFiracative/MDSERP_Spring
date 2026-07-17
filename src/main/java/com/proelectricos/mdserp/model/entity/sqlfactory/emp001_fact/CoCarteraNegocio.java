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
@Table(name = "co_cartera_negocio", catalog = "EMP001_FACT", schema = "dbo")
public class CoCarteraNegocio {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 120)
    @Column(name = "NOMBRE", length = 120)
    private String NOMBRE;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String CLIENTE;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String NIT;

    @Size(max = 7)
    @Column(name = "PEDIDO", length = 7)
    private String PEDIDO;

    @Size(max = 17)
    @NotNull
    @Column(name = "CONCEPTO", nullable = false, length = 17)
    private String CONCEPTO;

    @Size(max = 4)
    @Column(name = "PREFIJO", length = 4)
    private String PREFIJO;

    @Column(name = "NUMERO")
    private Integer NUMERO;

    @Column(name = "FECHA")
    private LocalDate FECHA;

    @Column(name = "VENCE")
    private LocalDate VENCE;

    @Column(name = "\"DIAS VENCIDO\"")
    private Integer DIAS_VENCIDO;

    @Column(name = "\"DIAS TOTALES\"")
    private Integer DIAS_TOTALES;

    @Column(name = "\"VALOR TOTAL\"", precision = 16, scale = 4)
    private BigDecimal VALOR_TOTAL;

    @Size(max = 50)
    @Nationalized
    @Column(name = "NEGOCIO", length = 50)
    private String NEGOCIO;

    @Column(name = "\"PORCENTAJE NEGOCIO\"", precision = 38, scale = 6)
    private BigDecimal PORCENTAJE_NEGOCIO;

    @Column(name = "\"VALOR PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal VALOR_PONDERADO;

    @Column(name = "\"ABONO TOTAL\"", precision = 16, scale = 4)
    private BigDecimal ABONO_TOTAL;

    @Column(name = "\"ABONO PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal ABONO_PONDERADO;

    @Column(name = "SALDO", precision = 17, scale = 4)
    private BigDecimal SALDO;

    @Column(name = "\"SALDO PONDERADO\"", precision = 38, scale = 6)
    private BigDecimal SALDO_PONDERADO;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String CIU;

    @Size(max = 30)
    @Column(name = "VENDEDOR", length = 30)
    private String VENDEDOR;


}
