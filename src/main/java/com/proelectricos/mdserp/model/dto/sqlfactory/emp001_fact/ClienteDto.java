package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Cliente}
 */
@Getter
@Setter
public class ClienteDto implements Serializable {
    Integer id;
    @Size(max = 15)
    String cod;
    @Size(max = 120)
    String nom;
    @Size(max = 200)
    String dir;
    @Size(max = 20)
    String ciu;
    @Size(max = 20)
    String aa;
    @Size(max = 30)
    String depto;
    @Size(max = 15)
    String nit;
    @Size(max = 4)
    String dist;
    @Size(max = 30)
    String condic;
    BigDecimal cupo;
    @Size(max = 3)
    String zona;
    @Size(max = 6)
    String ncod;
}