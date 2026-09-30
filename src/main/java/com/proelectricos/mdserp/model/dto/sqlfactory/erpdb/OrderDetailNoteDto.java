package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Nota de un pedido de ErpDb.
 * Construido desde {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderNote}.
 */
@Getter
@Setter
public class OrderDetailNoteDto implements Serializable {
    Integer posicion;
    String nota;
}
