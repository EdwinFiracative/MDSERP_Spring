package com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADIC", catalog = "emp004_inv", schema = "dbo")

public class Adic {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "course_gen")
    @TableGenerator(
            name = "course_gen",
            table = "IDS",
            catalog = "emp004_inv",
            schema = "dbo",
            pkColumnName = "TABLA",
            valueColumnName = "id",
            pkColumnValue = "adic",
            allocationSize = 2
    )
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "COD", length = 20)
    private String cod;

    @Column(name = "OP")
    private Integer op;

    @Column(name = "NOM", length = 40)
    private String nom;

    @Column(name = "VALOR", precision = 14)
    private BigDecimal valor;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal cant;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "SYS2015", length = 10)
    private String sys2015;

    @Column(name = "NUMOC")
    private Integer numoc;

    @Column(name = "CENTROOC", length = 10)
    private String centrooc;

    @Column(name = "NIT_OC", length = 15)
    private String nitOc;

    @Column(name = "CONSECDOC")
    private Integer consecdoc;

    @Column(name = "CONTADOC")
    private Integer contadoc;

    @Override
    public String toString() {
        return "Adic{" +
                "id=" + id +
                ", op='" + op + '\'' +
                ", fecha=" + fecha +
                '}';


    }
}
