package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ThirdParty}
 */
@Getter
@Setter
public class ThirdPartyDto implements Serializable {
    Long id;
    Long thirdPartyIdentNumber;
    Short thirdPartyVerifDigit;
    /** Numero de identificacion + "-" + digito de verificacion (si existe). */
    String nit;
    String thirdPartyName;
    String thirdPartyCity;
    String thirdPartyAddress;
    String thirdPartyPhoneNumber;
    String thirdPartyEmail;
}
