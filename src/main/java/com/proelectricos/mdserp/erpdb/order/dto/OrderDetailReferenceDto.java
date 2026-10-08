package com.proelectricos.mdserp.erpdb.order.dto;

import com.proelectricos.mdserp.erpdb.project.dto.ProjectDto;
import com.proelectricos.mdserp.erpdb.reference.dto.ReferenceDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Linea de un pedido de ErpDb.
 * Construido desde {@link com.proelectricos.mdserp.erpdb.order.OrderReference}.
 */
@Getter
@Setter
public class OrderDetailReferenceDto implements Serializable {
    Long orderReferId;
    Integer orderReferPosition;
    ReferenceDto orderReferReference;
    Integer orderReferQuantity;
    BigDecimal orderReferUnitPrice;
    /** Cantidad * valor unitario (sin impuestos); no existe en la base. */
    BigDecimal valorTotal;
    LocalDate orderReferDelivDate;
    /** Proyecto de la linea; null si no tiene. */
    ProjectDto orderReferProject;
    OrderReferStatusDto orderReferStatus;
}
