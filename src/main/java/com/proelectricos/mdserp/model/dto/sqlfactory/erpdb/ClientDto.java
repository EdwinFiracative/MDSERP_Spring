package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.Client}
 */
@Getter
@Setter
public class ClientDto implements Serializable {
    Long id;
    ThirdPartyDto clientThirdParty;
    String clientClassification;
    BigDecimal clientCrediLimit;
    String clientCrediCondition;
}
