package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TbPedidosResumenDto implements Serializable {

    @JsonProperty("NUM")
    @Size(max = 7)
    private String num;

    @JsonProperty("CLIENTE")
    @Size(max = 15)
    private String cliente;
}
