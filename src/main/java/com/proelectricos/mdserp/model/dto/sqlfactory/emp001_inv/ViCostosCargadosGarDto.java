package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ViCostosCargadosGarDto implements Serializable {

   /* @Size(max = 5)
    @JsonProperty("NumPedido")
    private String numPedido;

    @JsonProperty("OP")
    private Integer op;

    @Size(max = 120)
    @JsonProperty("Cliente")
    private String cliente;

    @JsonProperty("FechaInicio")
    private Instant fechaInicio;

    @JsonProperty("FechaCierre")
    private Instant fechaCierre;*/

    @JsonProperty("ValorTotal")
    private BigDecimal valorTotal;
}
