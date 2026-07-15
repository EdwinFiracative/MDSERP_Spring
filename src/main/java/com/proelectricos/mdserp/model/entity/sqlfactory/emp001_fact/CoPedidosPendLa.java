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
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "CO_PEDIDOS_PEND_LAS" ,  catalog = "EMP001_FACT", schema = "dbo")
public class CoPedidosPendLa {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "FECHA")
    private LocalDate FECHA;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String NUM;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String CLIENTE;

    @Size(max = 200)
    @Column(name = "NomCliente", length = 200)
    private String NomCliente;

    @Size(max = 5)
    @Column(name = "CodVend", length = 5)
    private String CodVend;

    @Size(max = 30)
    @Column(name = "NomVend", length = 30)
    private String NomVend;

    @Size(max = 20)
    @Column(name = "ciu", length = 20)
    private String ciu;

    @Size(max = 3)
    @Column(name = "zona", length = 3)
    private String zona;

    @Column(name = "ENTREGA")
    private LocalDate ENTREGA;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String COD;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String NOM;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String UD;

    @Size(max = 2)
    @Column(name = "GRUP", length = 2)
    private String GRUP;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal CANT;

    @Column(name = "COSTO", precision = 16, scale = 4)
    private BigDecimal COSTO;

    @Column(name = "PEND", precision = 14, scale = 4)
    private BigDecimal PEND;

    @Column(name = "ESTADO")
    private Character ESTADO;

    @Column(name = "NETO", precision = 16, scale = 4)
    private BigDecimal NETO;

    @Size(max = 50)
    @Column(name = "ORDEN", length = 50)
    private String ORDEN;

    @Size(max = 6)
    @Column(name = "NCOD", length = 6)
    private String NCOD;

    @Size(max = 30)
    @Column(name = "CONDIC", length = 30)
    private String CONDIC;

    @NotNull
    @Column(name = "DESPACHO", nullable = false, precision = 38, scale = 4)
    private BigDecimal DESPACHO;

    @Column(name = "CANTPEND", precision = 38, scale = 4)
    private BigDecimal CANTPEND;

    @Column(name = "VR PENDIENTE", precision = 38, scale = 6)
    private BigDecimal VR_PENDIENTE;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 50)
    private String Negocio;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "Linea_Neg_nombre", nullable = false, length = 80)
    private String Linea_Neg_nombre;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "SubLinea_Neg_nom", nullable = false, length = 50)
    private String SubLinea_Neg_nom;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "Grupo_Neg_nom", nullable = false, length = 60)
    private String Grupo_Neg_nom;


}
