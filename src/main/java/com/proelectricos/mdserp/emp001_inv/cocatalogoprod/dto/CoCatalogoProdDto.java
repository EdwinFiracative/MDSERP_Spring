package com.proelectricos.mdserp.emp001_inv.cocatalogoprod.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.emp001_inv.cocatalogoprod.CoCatalogoProd}
 */
@Getter
@Setter
public class CoCatalogoProdDto implements Serializable {
    @Size(max = 20)
    private String cod;
    @Size(max = 100)
    private String nom;
    @Size(max = 2)
    private String ud;
    @Size(max = 2)
    private String grup;
    @Size(max = 40)
    private String nomGrupo;
    @Size(max = 6)
    private String ubic;
    private BigDecimal exist;
    private BigDecimal cstd;
    private Character activo;
    @Size(max = 30)
    private String negocio;
    @Size(max = 50)
    private String linea;
    @Size(max = 80)
    private String clase;
    @Size(max = 50)
    private String grupo;
    @Size(max = 60)
    private String nivel5;
}


