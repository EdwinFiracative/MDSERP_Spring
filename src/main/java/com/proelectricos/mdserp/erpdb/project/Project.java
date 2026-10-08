package com.proelectricos.mdserp.erpdb.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Project", catalog = "ErpDb", schema = "dbo")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projeId", nullable = false)
    private Long id;

    @Size(max = 250)
    @NotNull
    @Nationalized
    @Column(name = "projeName", nullable = false, length = 250)
    private String projeName;


}
