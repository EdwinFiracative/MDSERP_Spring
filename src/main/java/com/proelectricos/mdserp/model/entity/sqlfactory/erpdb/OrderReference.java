package com.proelectricos.mdserp.model.entity.sqlfactory.erpdb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "OrderReference")
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

    @Size(max = 1)
    @Nationalized
    @Column(name = "orderReferApproState", length = 1)
    private String orderReferApproState;

    @NotNull
    @Column(name = "orderReferDelivDate", nullable = false)
    private LocalDate orderReferDelivDate;


}