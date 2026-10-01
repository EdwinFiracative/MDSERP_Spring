package com.proelectricos.mdserp.erpdb.client.dto;

import com.proelectricos.mdserp.erpdb.thirdparty.dto.ThirdPartyDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.client.Client}
 */
@Getter
@Setter
public class ClientDto implements Serializable {
    Long clientId;
    ThirdPartyDto clientThirdParty;
}
