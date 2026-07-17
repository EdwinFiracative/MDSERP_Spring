package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_ofer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonPropertyOrder({
        "NUM",
        "fi_ohe_code"
})
public class VwAvioferpedidoDto implements Serializable {

    @JsonProperty(value = "NUM", access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 7)
    private String NUM;

    @JsonProperty("fi_ohe_code")
    @NotNull
    private Integer fi_ohe_code;
}
