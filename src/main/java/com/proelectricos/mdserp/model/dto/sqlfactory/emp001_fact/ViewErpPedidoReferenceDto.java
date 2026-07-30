package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.ReferenciaBasicDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoReference;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Referencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link ViewErpPedidoReference}
 */
@Getter
@Setter
public class ViewErpPedidoReferenceDto implements Serializable {
    Long id;
    Long pos;
    ReferenciaBasicDto cod;
    @Size(max = 200)
    String nom;
    @Size(max = 2)
    String ud;
    BigDecimal cant;
    BigDecimal costo;
    BigDecimal pend;
    Character estado;
    BigDecimal neto;
    Instant entrega;
    @NotNull
    Integer factoryId;
}