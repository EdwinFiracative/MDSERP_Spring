package com.proelectricos.mdserp.model.entity.sqlfactory.ErpDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientId", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clientThirdParty", nullable = false)
    private ThirdParty clientThirdParty;

    @Size(max = 4)
    @Nationalized
    @Column(name = "clientClassification", length = 4)
    private String clientClassification;

    @Column(name = "clientCrediLimit")
    private BigDecimal clientCrediLimit;

    @Size(max = 30)
    @Nationalized
    @Column(name = "clientCrediCondition", length = 30)
    private String clientCrediCondition;

    @OneToMany(mappedBy = "branchClient")
    private Set<Branch> branches = new LinkedHashSet<>();


}