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
@Table(name = "ThirdParty")
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thirdPartyId", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "thirdPartyIdentNumber", nullable = false)
    private Long thirdPartyIdentNumber;

    @Column(name = "thirdPartyVerifDigit", columnDefinition = "tinyint")
    private Short thirdPartyVerifDigit;

    @Size(max = 120)
    @NotNull
    @Nationalized
    @Column(name = "thirdPartyName", nullable = false, length = 120)
    private String thirdPartyName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "thirdPartyCity", length = 50)
    private String thirdPartyCity;

    @Size(max = 200)
    @Nationalized
    @Column(name = "thirdPartyAddress", length = 200)
    private String thirdPartyAddress;

    @Size(max = 20)
    @Nationalized
    @Column(name = "thirdPartyPhoneNumber", length = 20)
    private String thirdPartyPhoneNumber;

    @Size(max = 80)
    @Nationalized
    @Column(name = "thirdPartyEmail", length = 80)
    private String thirdPartyEmail;


}