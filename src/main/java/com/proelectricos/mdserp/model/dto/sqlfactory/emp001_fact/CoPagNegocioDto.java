package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagNegocio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link CoPagNegocio}
 */
@Getter
@Setter
public class CoPagNegocioDto implements Serializable {

   // private Long id;
    @Size(max = 4)
    private String prefijo;
    private Integer numero;
    private LocalDate fecfact;
    @Size(max = 15)
    private String cliente;
    @Size(max = 15)
    private String nit;
    @Size(max = 120)
    private String nom;
    @NotNull
    @Size(max = 25)
    private String periodo;
    private BigDecimal vlrFra;
    private Character estado;
    @Size(max = 4)
    private String conc;
    @Size(max = 6)
    private String recibo;
    private LocalDate fecrec;
    @NotNull
    @Size(max = 25)
    private String periodoRecibo;
    private BigDecimal valorTotal;
    @Size(max = 50)
    private String negocio;
    private BigDecimal porcentajeNegocio;
    private BigDecimal valorPonderado;
    @Size(max = 2)
    private String banco;
    @Size(max = 20)
    private String entidad;
    @Size(max = 5)
    private String vendedor;
    @Size(max = 30)
    private String ejecutivo;
    private BigDecimal valorx;
    @Size(max = 25)
    private String ciu;
    private BigDecimal vrMcia;
    @Size(max = 50)
    private String concepto;
    private LocalDate fdcto;
}


