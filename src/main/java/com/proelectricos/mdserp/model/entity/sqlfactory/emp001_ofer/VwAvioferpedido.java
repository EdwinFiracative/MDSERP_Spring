package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_ofer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_aviOferPedidos", catalog = "EMP001_OFER", schema = "dbo")
public class VwAvioferpedido {
   @Id
    @NotNull
    @Column(name = "fi_ohe_code", nullable = false)
    private Integer fi_ohe_code;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String NUM;
}