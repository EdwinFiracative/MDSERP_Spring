package com.proelectricos.mdserp.emp001_fact.viewerppedido.dto;

import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoNote;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ViewErpPedidoNote}
 */
@Getter
@Setter
public class ViewErpPedidoNoteDto implements Serializable {
    Long id;
    @Size(max = 20)
    String cod;
    @Size(max = 200)
    String nom;
}