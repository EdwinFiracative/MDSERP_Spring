package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido}
 */
@Getter
@Setter
public class PedidoDto implements Serializable {
    @NotNull
    Integer id;
    @Size(max = 15)
    String destinat;
    @Size(max = 250)
    String tdespacho;
    @Size(max = 7)
    String num;
    @Size(max = 15)
    String cliente;
    Instant fecha;
    @Size(max = 20)
    String cod;
    @Size(max = 200)
    String nom;
    @Size(max = 2)
    String ud;
    BigDecimal cant;
    BigDecimal costo;
    BigDecimal pend;
    Character estado;
    BigDecimal desp;
    BigDecimal desc3;
    @Size(max = 4)
    String zona;
    @Size(max = 5)
    String vendedor;
    Instant entrega;
    @Size(max = 50)
    String orden;
    @Size(max = 6)
    String ncod;
    Instant fechan;
    @Size(max = 10)
    String sys2015;
    String detalle;
    @Size(max = 30)
    String condic;
}