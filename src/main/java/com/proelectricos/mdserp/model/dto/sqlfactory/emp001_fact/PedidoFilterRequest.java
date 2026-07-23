package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class PedidoFilterRequest {
    private List<String> num;
    private List<String> tdespacho;
    private List<String> cliente;
    private List<String> cod;
    private List<String> vendedor;
    private Instant fechaBefore;
    private Instant fechaAfter;
    private Character estado;
}
