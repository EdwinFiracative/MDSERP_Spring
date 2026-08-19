package com.proelectricos.mdserp.model.entity.sqlfactory.ErpDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Reference")
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referId", nullable = false)
    private Long id;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "referCod", nullable = false, length = 60)
    private String referCod;

    @Size(max = 60)
    @Nationalized
    @Column(name = "referCod2", length = 60)
    private String referCod2;

    @Size(max = 200)
    @NotNull
    @Nationalized
    @Column(name = "referName", nullable = false, length = 200)
    private String referName;

    @Size(max = 200)
    @Nationalized
    @Column(name = "referDescription", length = 200)
    private String referDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referClassification")
    private ReferClassification referClassification;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referMeasuUnit", nullable = false)
    private MeasurUnit referMeasuUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referAlterMeasuUnit")
    private MeasurUnit referAlterMeasuUnit;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "referConveFactor", nullable = false, precision = 14, scale = 4)
    private BigDecimal referConveFactor;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "referActive", nullable = false)
    private Boolean referActive;

    @NotNull
    @Column(name = "referStandCost", nullable = false)
    private BigDecimal referStandCost;

    @NotNull
    @Column(name = "referIva", nullable = false, precision = 8, scale = 4)
    private BigDecimal referIva;

    @NotNull
    @Column(name = "referIca", nullable = false, precision = 8, scale = 4)
    private BigDecimal referIca;

    @NotNull
    @Column(name = "referWithholding", nullable = false, precision = 8, scale = 4)
    private BigDecimal referWithholding;


}