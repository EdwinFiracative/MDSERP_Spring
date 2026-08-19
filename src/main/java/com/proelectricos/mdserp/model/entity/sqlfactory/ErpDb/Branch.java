package com.proelectricos.mdserp.model.entity.sqlfactory.ErpDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branchId", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branchClient", nullable = false)
    private Client branchClient;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "branchCity", nullable = false, length = 50)
    private String branchCity;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "branchAddress", nullable = false)
    private String branchAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branchVendor")
    private Vendor branchVendor;

    @Size(max = 2)
    @NotNull
    @Nationalized
    @Column(name = "branchCode", nullable = false, length = 2)
    private String branchCode;

    @OneToMany(mappedBy = "orderHeaderBranch")
    private Set<OrderHeader> orderHeaders = new LinkedHashSet<>();


}