package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;


@Entity
@Table(name = "op1", catalog = "emp001_inv", schema = "dbo")
public class Op1 {

    @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableMaxGenerator")
    @GenericGenerator(name = "tableMaxGenerator", strategy =
            "com.proelectricos.mdserp.model.entity.sqlfactory.generator.Op1IdGenerator")
    @Column(name = "ID", nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Column(name = "OP")
    @Getter
    @Setter
    private Integer op;


    //@Getter
    //@Setter
    private String cod;

    @Column(name = "CANT", precision = 14, scale = 4)
    @Getter
    @Setter
    private BigDecimal cant;

    @Column(name = "CANT1", precision = 14, scale = 4)
    @Getter
    @Setter
    private BigDecimal cant1;

 @Column(name = "COD", length = 20)
    public String getCod() {
        return cod + "get";
    }

    public void setCod(String cod) {
        this.cod = cod +  "set";
    }
}
