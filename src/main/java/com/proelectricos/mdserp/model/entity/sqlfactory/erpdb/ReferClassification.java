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
@Table(name = "ReferClassification", catalog = "ErpDb", schema = "dbo")
public class ReferClassification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referClassId", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referClassFather")
    private ReferClassification referClassFather;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "referClassName", nullable = false, length = 100)
    private String referClassName;

    @Size(max = 200)
    @Nationalized
    @Column(name = "referClassDescription", length = 200)
    private String referClassDescription;

    @Size(max = 11)
    @Nationalized
    @Column(name = "referClassGroupFactory", length = 11)
    private String referClassGroupFactory;

    @OneToMany(mappedBy = "referClassFather")
    private Set<ReferClassification> ReferClassificationSons = new LinkedHashSet<>();

}