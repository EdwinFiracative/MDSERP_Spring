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
public class ViTimeMastSemieOpDto implements Serializable {

    /*private Long id;*/

    @JsonProperty("OP")
    private Integer op;

    @Size(max = 20)
    @JsonProperty("COD")
    private String cod;

    @Size(max = 100)
    @JsonProperty("NOM")
    private String nom;

    @Size(max = 2)
    @JsonProperty("UD")
    private String ud;

    @Size(max = 2)
    @JsonProperty("GRUP")
    private String grup;

    @JsonProperty("CANTP")
    private BigDecimal cantp;

    @JsonProperty("CANTE")
    private BigDecimal cante;

    @JsonProperty("FECHA_I")
    private Instant fechaI;

    @JsonProperty("FECHA_T")
    private Instant fechaT;

    @JsonProperty("ESTADO")
    private Character estado;

    @Size(max = 20)
    @JsonProperty("CODMAST")
    private String codmast;

    @Size(max = 100)
    @JsonProperty("NOM MASTER")
    private String nomMaster;

    @Size(max = 4)
    @JsonProperty("CECO")
    private String ceco;

    @JsonProperty("HORAS")
    private BigDecimal horas;
}
