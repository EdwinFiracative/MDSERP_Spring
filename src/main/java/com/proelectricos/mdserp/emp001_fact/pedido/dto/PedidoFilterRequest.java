package com.proelectricos.mdserp.emp001_fact.pedido.dto;

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
