package com.proelectricos.mdserp.erpdb.thirdparty.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.thirdparty.ThirdParty}
 */
@Getter
@Setter
public class ThirdPartyDto implements Serializable {
    Long thirdPartyId;
    Long thirdPartyIdentNumber;
    String thirdPartyName;
}
