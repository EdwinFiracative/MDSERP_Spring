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
import java.time.LocalDate;

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
    private String NUM;

    @Size(max = 50)
    @Column(name = "ORDEN", length = 50)
    private String ORDEN;

    @Column(name = "FECHA")
    private LocalDate FECHA;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String CLIENTE;

    @Size(max = 15)
    @Column(name = "NIT", length = 15)
    private String NIT;

    @Size(max = 120)
    @Column(name = "\"NOMBRE DEL CLIENTE\"", length = 120)
    private String NOMBRE_DEL_CLIENTE;

    @Size(max = 20)
    @Column(name = "CIU", length = 20)
    private String CIU;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String VENDEDOR;

    @Size(max = 30)
    @Column(name = "\"NOMBRE DEL VENDEDOR\"", length = 30)
    private String NOMBRE_DEL_VENDEDOR;

    @Column(name = "\"COSTO ITEM\"", precision = 38, scale = 8)
    private BigDecimal COSTO_ITEM;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 30)
    private String Negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 50)
    private String Clase;


}
