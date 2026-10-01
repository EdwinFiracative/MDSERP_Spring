package com.proelectricos.mdserp.emp001_fact.vendedor.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.emp001_fact.vendedor.Vendedor}
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