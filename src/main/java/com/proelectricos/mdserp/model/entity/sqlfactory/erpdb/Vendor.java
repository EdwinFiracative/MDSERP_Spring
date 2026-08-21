package com.proelectricos.mdserp.model.entity.sqlfactory.erpdb;

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
@Table(name = "Vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendorId", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendorThirdParty", nullable = false)
    private ThirdParty vendorThirdParty;

    @Size(max = 5)
    @NotNull
    @Nationalized
    @Column(name = "vendorCode", nullable = false, length = 5)
    private String vendorCode;

    @OneToMany(mappedBy = "branchVendor")
    private Set<Branch> branches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderHeaderVendor")
    private Set<OrderHeader> orderHeaders = new LinkedHashSet<>();


}