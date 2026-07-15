package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoFacturacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link CoFacturacion}
 */
@Getter
@Setter
public class CoFacturacionDto implements Serializable {
    Long id;
    Integer factura;
    @Size(max = 4)
    String prefijo;
    @Size(max = 10)
    String pedid;
    Instant fftra;
    @Size(max = 7)
    String periodo;
    Integer año;
    @Size(max = 30)
    String mes;
    @Size(max = 15)
    String codCliente;
    @Size(max = 120)
    String nomCliente;
    Character estado;
    @Size(max = 4)
    String dist;
    @Size(max = 40)
    String nomDist;
    @Size(max = 3)
    String zona;
    @Size(max = 20)
    String ciu;
    @Size(max = 5)
    String codVdor;
    @Size(max = 30)
    String nomVdor;
    @Size(max = 20)
    String cod;
    @Size(max = 100)
    String descripcion;
    @Size(max = 100)
    String nom1;
    BigDecimal cant;
    BigDecimal vlrUnit;
    BigDecimal subtotalLinea;
    @NotNull
    BigDecimal cantDev;
    @NotNull
    BigDecimal valorDevolucion;
    @NotNull
    BigDecimal costoKardex;
    @NotNull
    BigDecimal costoDevKardex;
    BigDecimal cstStand;
    @NotNull
    @Size(max = 30)
    String negocio;
    @NotNull
    @Size(max = 50)
    String clase;
    @NotNull
    @Size(max = 80)
    String linea;
    @NotNull
    @Size(max = 50)
    String grupo;
    @NotNull
    @Size(max = 60)
    String nivel;
    BigDecimal total;
    BigDecimal ventaneta;
    BigDecimal costoTotalFinal;
    BigDecimal utilidad;
    BigDecimal caMargen;
}

