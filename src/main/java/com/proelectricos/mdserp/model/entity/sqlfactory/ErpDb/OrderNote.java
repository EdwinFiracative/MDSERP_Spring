package com.proelectricos.mdserp.model.entity.sqlfactory.ErpDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "OrderNote")
public class OrderNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderNoteId", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderNoteOrderHeader", nullable = false)
    private OrderHeader orderNoteOrderHeader;

    @NotNull
    @Column(name = "orderNotePosition", nullable = false)
    private Integer orderNotePosition;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "orderNoteText", nullable = false)
    private String orderNoteText;


}