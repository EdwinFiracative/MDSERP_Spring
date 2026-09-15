package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Immutable
@Table(name = "viCostosCargadosGar", catalog = "EMP001_INV", schema = "dbo")
public class ViCostosCargadosGar {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 5)
    @Column(name = "NumPedido", length = 5)
    private String numPedido;

    @Column(name = "OP")
    private Integer op;

    @Size(max = 120)
    @Column(name = "Cliente", length = 120)
    private String cliente;

    @Column(name = "FechaInicio")
    private Instant fechaInicio;

    @Column(name = "FechaCierre")
    private Instant fechaCierre;

    @Column(name = "ValorTotal", precision = 38)
    private BigDecimal valorTotal;


}