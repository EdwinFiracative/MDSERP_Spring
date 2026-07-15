package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagos}
 */
@Getter
@Setter
public class CoPagosDto implements Serializable {
    //Long id;
    @Size(max = 4)
    String prefijo;
    Integer numero;
    LocalDate fecfact;
    @Size(max = 15)
    String cliente;
    @Size(max = 15)
    String nit;
    @Size(max = 120)
    String nom;
    @NotNull
    @Size(max = 25)
    String periodo;
    BigDecimal vlrFra;
    Character estado;
    Integer numFactPagos;
    @Size(max = 4)
    String conc;
    @Size(max = 6)
    String recibo;
    LocalDate fecrec;
    BigDecimal valor;
    @Size(max = 2)
    String banco;
    @Size(max = 20)
    String entidad;
    @Size(max = 120)
    String deta;
    @Size(max = 5)
    String vendedor;
    @Size(max = 30)
    String ejecutivo;
    BigDecimal valorx;
    BigDecimal costoEstandar;
    BigDecimal margen;
    @Size(max = 25)
    String ciu;
    BigDecimal vrMcia;
    @Size(max = 50)
    String concepto;
    LocalDate fdcto;
    Integer diasPagoFecFactura;
    Integer diasPagoFecVcto;
    Integer añoRecibo;
    @NotNull
    @Size(max = 25)
    String periodoRecibo;
    @Size(max = 5)
    String pedido;
    @Size(max = 7)
    String num;
    @Size(max = 50)
    String lineas;
    @NotNull
    @Size(max = 10)
    String cumplimientoPagos;
}

