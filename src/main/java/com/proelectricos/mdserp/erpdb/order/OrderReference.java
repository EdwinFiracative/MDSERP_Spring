package com.proelectricos.mdserp.erpdb.order;

import com.proelectricos.mdserp.erpdb.project.Project;
import com.proelectricos.mdserp.erpdb.reference.Reference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "OrderReference", catalog = "ErpDb", schema = "dbo")
public class OrderReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderReferId", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderReferOrderHeader", nullable = false)
    private OrderHeader orderReferOrderHeader;

    @NotNull
    @Column(name = "orderReferPosition", nullable = false)
    private Integer orderReferPosition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderReferReference", nullable = false)
    private Reference orderReferReference;

    @NotNull
    @Column(name = "orderReferQuantity", nullable = false)
    private Integer orderReferQuantity;

    @NotNull
    @Column(name = "orderReferUnitPrice", nullable = false)
    private BigDecimal orderReferUnitPrice;

    @NotNull
    @Column(name = "orderReferDelivDate", nullable = false)
    private LocalDate orderReferDelivDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderReferProject")
    private Project orderReferProject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderReferStatus", nullable = false)
    private OrderReferStatus orderReferStatus;


}