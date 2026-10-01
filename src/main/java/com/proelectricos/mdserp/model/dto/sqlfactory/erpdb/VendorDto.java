package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.Vendor}
 */
@Getter
@Setter
public class VendorDto implements Serializable {
    Long id;
    ThirdPartyDto vendorThirdParty;
    String vendorCode;
}
