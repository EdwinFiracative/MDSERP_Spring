package com.proelectricos.mdserp.model.dto.sqlfactory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.CoCarteraNegocio}
 */
@Getter
@Setter
public class CoCarteraNegocioDto implements Serializable {
    private Long id;
    @Size(max = 120)
    private String nombre;
    @Size(max = 15)
    private String cliente;
    @Size(max = 15)
    private String nit;
    @Size(max = 7)
    private String pedido;
    @NotNull
    @Size(max = 17)
    private String concepto;
    @Size(max = 4)
    private String prefijo;
    private Integer numero;
    private LocalDate fecha;
    private LocalDate vence;
    private Integer diasVencido;
    private Integer diasTotales;
    private BigDecimal valorTotal;
    @Size(max = 50)
    private String negocio;
    private BigDecimal porcentajeNegocio;
    private BigDecimal valorPonderado;
    private BigDecimal abonoTotal;
    private BigDecimal abonoPonderado;
    private BigDecimal saldo;
    private BigDecimal saldoPonderado;
    @Size(max = 20)
    private String ciu;
    @Size(max = 30)
    private String vendedor;
}
