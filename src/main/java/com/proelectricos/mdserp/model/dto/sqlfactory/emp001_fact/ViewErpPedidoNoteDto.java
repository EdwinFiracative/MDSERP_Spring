package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoNote;
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