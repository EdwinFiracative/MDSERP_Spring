package com.proelectricos.mdserp.erpdb.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Nota de un pedido de ErpDb.
 * Construido desde {@link com.proelectricos.mdserp.erpdb.order.OrderNote}.
 */
@Getter
@Setter
public class OrderDetailNoteDto implements Serializable {
    Long orderNoteId;
    Integer orderNotePosition;
    String orderNoteText;
}
