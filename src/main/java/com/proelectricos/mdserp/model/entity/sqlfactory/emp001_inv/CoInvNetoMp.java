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

@Getter
@Entity
@Immutable
@Table(name = "Co_Inv_Neto_MP", catalog = "EMP001_INV", schema = "dbo")
public class CoInvNetoMp {

    @Id
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

    @Column(name = "CATEG")
    private Character CATEG;

    @Size(max = 40)
    @Column(name = "CLASE_PROD", length = 40)
    private String CLASE_PROD;

    @Size(max = 20)
    @Column(name = "CODMP", length = 20)
    private String CODMP;

    @NotNull
    @Column(name = "REQUISITO", nullable = false, precision = 38, scale = 4)
    private BigDecimal REQUISITO;

    @Column(name = "EXIST", precision = 14, scale = 4)
    private BigDecimal EXIST;

    @Column(name = "CANT_OC", precision = 14, scale = 4)
    private BigDecimal CANT_OC;

    @Column(name = "SMAX", precision = 14, scale = 4)
    private BigDecimal SMAX;

    @Column(name = "COMPRAR", precision = 38, scale = 4)
    private BigDecimal COMPRAR;

    @Column(name = "\"VALOR Existencia\"", precision = 14)
    private BigDecimal VALOR_Existencia;

    @Column(name = "SMIN", precision = 14, scale = 4)
    private BigDecimal SMIN;

    @Column(name = "\"Costo repo\"", precision = 16, scale = 4)
    private BigDecimal Costo_repo;

    @Column(name = "\"Costo STD\"", precision = 16, scale = 4)
    private BigDecimal Costo_STD;

    @Column(name = "\"Cons Prom\"", precision = 38, scale = 6)
    private BigDecimal Cons_Prom;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "Negocio", nullable = false, length = 30)
    private String Negocio;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Linea", nullable = false, length = 50)
    private String Linea;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 80)
    private String Clase;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Grupo", nullable = false, length = 50)
    private String Grupo;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "Nivel5", nullable = false, length = 60)
    private String Nivel5;

    @Column(name = "\"LEAD TIMES\"")
    private Integer LEAD_TIMES;

    @Column(name = "PRM_CONS", precision = 38, scale = 6)
    private BigDecimal PRM_CONS;

    @Column(name = "DESVSTD")
    private Double DESVSTD;


}
