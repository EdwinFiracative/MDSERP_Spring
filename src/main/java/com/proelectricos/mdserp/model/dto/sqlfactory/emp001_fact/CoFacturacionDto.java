package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoFacturacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link CoFacturacion}
 */
@Getter
@Setter
public class CoFacturacionDto implements Serializable {
    Long id;
    Integer factura;
    @Size(max = 4)
    String PREFIJO;
    @Size(max = 10)
    String PEDID;
    LocalDate fftra;
    @Size(max = 7)
    String periodo;
    Integer año;
    @Size(max = 30)
    String mes;
    @Size(max = 15)
    String cod_cliente;
    @Size(max = 120)
    String nom_cliente;
    Character ESTADO;
    @Size(max = 4)
    String DIST;
    @Size(max = 40)
    String nom_dist;
    @Size(max = 3)
    String ZONA;
    @Size(max = 20)
    String CIU;
    @Size(max = 5)
    String cod_vdor;
    @Size(max = 30)
    String nom_vdor;
    @Size(max = 20)
    String COD;
    @Size(max = 100)
    String descripcion;
    @Size(max = 100)
    String NOM1;
    BigDecimal cant;
    BigDecimal vlr_unit;
    BigDecimal subtotal_linea;
    @NotNull
    BigDecimal cant_dev;
    @NotNull
    BigDecimal valor_devolucion;
    @NotNull
    BigDecimal costo_kardex;
    @NotNull
    BigDecimal costo_dev_kardex;
    BigDecimal cst_stand;
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
    BigDecimal costo_total_final;
    BigDecimal utilidad;
    BigDecimal ca_margen;
}

