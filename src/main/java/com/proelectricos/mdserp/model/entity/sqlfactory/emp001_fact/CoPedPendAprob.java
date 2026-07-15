package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Immutable
@Table(name = "co_ped_pend_aprob", catalog = "EMP001_FACT", schema = "dbo")
public class CoPedPendAprob {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String num;

    @Size(max = 50)
    @Column(name = "ORDEN", length = 50)
    private String orden;

    @Column(name = "FECHA")
    private Instant fecha;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String cliente;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String nit;

    @Size(max = 120)
    @Column(name = "\"NOMBRE DEL CLIENTE\"", length = 120)
    private String nombreDelCliente;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String ciu;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String vendedor;

    @Size(max = 30)
    @Column(name = "\"NOMBRE DEL VENDEDOR\"", length = 30)
    private String nombreDelVendedor;

    @Column(name = "\"COSTO ITEM\"", precision = 38, scale = 8)
    private BigDecimal costoItem;

    @Column(name = "ESTADO")
    private Character estado;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 30)
    private String negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 50)
    private String clase;


}
