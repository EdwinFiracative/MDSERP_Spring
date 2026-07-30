package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Vendedor}
 */
@Getter
@Setter
public class VendedorDto implements Serializable {
    Integer id;
    @Size(max = 5)
    String cod;
    @Size(max = 30)
    String nom;
}