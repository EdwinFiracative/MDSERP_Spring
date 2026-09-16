package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.IndicadoresSeguimiento}
 */
@Getter
@Setter
public class IndicadoresSeguimientoDto implements Serializable {

    /*private Long id;*/

    @Size(max = 7)
    private String num;

    @Size(max = 15)
    private String cliente;

    @Size(max = 120)
    private String nomCliente;

    @Size(max = 20)
    private String ciu;

    @Size(max = 5)
    private String vendedor;

    @Size(max = 30)
    private String nomV;

    @Size(max = 6)
    private String ncod;

    @Size(max = 30)
    private String nomVend;

    private LocalDate fecha;

    @Size(max = 20)
    private String cod;

    @Size(max = 100)
    private String nom;

    @Size(max = 2)
    private String ud;

    @Size(max = 2)
    private String grup;

    private Character estado;

    private BigDecimal cstd;

    private BigDecimal cant;

    private BigDecimal costoEstandar;

    private BigDecimal neto;

    private BigDecimal valorVenta;

    private BigDecimal sumCant;

    private BigDecimal costoVenta;

    private BigDecimal margenStandar;

    private BigDecimal margenReal;

    @NotNull
    @Size(max = 50)
    private String clase;
}
