package com.proelectricos.mdserp.erpdb.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.order.OrderReferStatus}
 */
@Getter
@Setter
public class OrderReferStatusDto implements Serializable {
    Long orderReferStatusId;
    String orderReferStatusName;
}
