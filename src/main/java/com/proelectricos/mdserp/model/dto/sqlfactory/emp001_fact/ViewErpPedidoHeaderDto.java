package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader}
 */
@Getter
@Setter
public class ViewErpPedidoHeaderDto implements Serializable {
    @Size(max = 7)
    String num;
    @Size(max = 15)
    String destinat;
    @Size(max = 250)
    String tdespacho;
    @Size(max = 15)
    String cliente;
    Instant fecha;
    @Size(max = 5)
    String vendedor;
    @Size(max = 50)
    String orden;
    @Size(max = 6)
    String ncod;
    String detalle;
    @Size(max = 30)
    String condic;
    @NotNull
    Integer factoryId;
    List<ViewErpPedidoReferenceDto> references;
}