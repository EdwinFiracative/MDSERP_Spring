package com.proelectricos.mdserp.erpdb.vendor.dto;

import com.proelectricos.mdserp.erpdb.thirdparty.dto.ThirdPartyDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.vendor.Vendor}
 */
@Getter
@Setter
public class VendorDto implements Serializable {
    Long vendorId;
    ThirdPartyDto vendorThirdParty;
    String vendorCode;
}
