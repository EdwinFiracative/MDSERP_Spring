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
@Table(name = "MeasurUnit")
public class MeasurUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measuUnitId", nullable = false)
    private Long id;

    @Size(max = 3)
    @NotNull
    @Nationalized
    @Column(name = "measuUnitCode", nullable = false, length = 3)
    private String measuUnitCode;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "measuUnitName", nullable = false, length = 50)
    private String measuUnitName;

    @Size(max = 10)
    @NotNull
    @Nationalized
    @Column(name = "measuUnitDianCode", nullable = false, length = 10)
    private String measuUnitDianCode;

    @OneToMany(mappedBy = "referMeasuUnit")
    private Set<Reference> references = new LinkedHashSet<>();


}