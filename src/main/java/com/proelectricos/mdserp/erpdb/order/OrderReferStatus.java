package com.proelectricos.mdserp.erpdb.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "OrderReferStatus", catalog = "ErpDb", schema = "dbo")
public class OrderReferStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderReferStatusId", nullable = false)
    private Long id;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "orderReferStatusName", nullable = false, length = 60)
    private String orderReferStatusName;

    @Size(max = 250)
    @NotNull
    @Nationalized
    @Column(name = "orderReferStatusDescription", nullable = false, length = 250)
    private String orderReferStatusDescription;


}
