package com.proelectricos.mdserp.erpdb.order;

import com.proelectricos.mdserp.erpdb.branch.Branch;
import com.proelectricos.mdserp.erpdb.vendor.Vendor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OrderHeader", catalog = "ErpDb", schema = "dbo")
public class OrderHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderHeaderId", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "orderHeaderDate", nullable = false)
    private LocalDate orderHeaderDate;

    @NotNull
    @Column(name = "orderHeaderNumber", nullable = false)
    private Integer orderHeaderNumber;

    @Size(max = 100)
    @Nationalized
    @Column(name = "orderHeaderDescription", length = 100)
    private String orderHeaderDescription;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderHeaderBranch", nullable = false)
    private Branch orderHeaderBranch;

    @Size(max = 250)
    @Nationalized
    @Column(name = "orderHeaderProject", length = 250)
    private String orderHeaderProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderHeaderVendor")
    private Vendor orderHeaderVendor;

    @Size(max = 60)
    @Nationalized
    @Column(name = "orderHeaderClientOrder", length = 60)
    private String orderHeaderClientOrder;

    @Size(max = 30)
    @Nationalized
    @Column(name = "orderHeaderPaymeConditions", length = 30)
    private String orderHeaderPaymeConditions;

    @ColumnDefault("sysdatetime()")
    @Generated(event = EventType.INSERT)
    @Column(name = "orderHeaderCreatTimeStamp", nullable = false, insertable = false, updatable = false)
    private LocalDateTime orderHeaderCreatTimeStamp;

    @OneToMany(mappedBy = "orderNoteOrderHeader")
    private Set<OrderNote> orderNotes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderReferOrderHeader")
    private Set<OrderReference> orderReferences = new LinkedHashSet<>();


}