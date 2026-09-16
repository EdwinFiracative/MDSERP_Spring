package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "vw_Pedidos_Clasificados_2", catalog = "EMP001_INV", schema = "dbo")
public class IndicadoresSeguimiento {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @Column(name = "num", length = 7)
    private String num;

    @Size(max = 15)
    @Column(name = "cliente", length = 15)
    private String cliente;

    @Size(max = 120)
    @Column(name = "nom_cliente", length = 120)
    private String nomCliente;

    @Size(max = 20)
    @Column(name = "ciu", length = 20)
    private String ciu;

    @Size(max = 5)
    @Column(name = "vendedor", length = 5)
    private String vendedor;

    @Size(max = 30)
    @Column(name = "nom_v", length = 30)
    private String nomV;

    @Size(max = 6)
    @Column(name = "ncod", length = 6)
    private String ncod;

    @Size(max = 30)
    @Column(name = "nom_vend", length = 30)
    private String nomVend;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Size(max = 20)
    @Column(name = "cod", length = 20)
    private String cod;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Size(max = 2)
    @Column(name = "ud", length = 2)
    private String ud;

    @Size(max = 2)
    @Column(name = "grup", length = 2)
    private String grup;

    @Column(name = "estado")
    private Character estado;

    @Column(name = "cstd", precision = 16, scale = 4)
    private BigDecimal cstd;

    @Column(name = "cant", precision = 14, scale = 4)
    private BigDecimal cant;

    @Column(name = "costo_estandar", precision = 31, scale = 8)
    private BigDecimal costoEstandar;

    @Column(name = "neto", precision = 16, scale = 4)
    private BigDecimal neto;

    @Column(name = "valor_venta", precision = 31, scale = 8)
    private BigDecimal valorVenta;

    @Column(name = "sum_cant", precision = 38, scale = 4)
    private BigDecimal sumCant;

    @Column(name = "costo_venta", precision = 38)
    private BigDecimal costoVenta;

    @Column(name = "margen_standar", precision = 38, scale = 6)
    private BigDecimal margenStandar;

    @Column(name = "margen_real", precision = 38, scale = 6)
    private BigDecimal margenReal;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "clase", nullable = false, length = 50)
    private String clase;


}