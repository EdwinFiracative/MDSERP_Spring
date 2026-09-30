package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Linea de un pedido de ErpDb.
 * Construido desde {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderReference}.
 */
@Getter
@Setter
public class OrderDetailReferenceDto implements Serializable {
    Integer item;
    String codigoReferencia;
    String nombre;
    String ud;
    Integer cantidad;
    BigDecimal valorUnitario;
    /** Cantidad * valor unitario (sin impuestos). */
    BigDecimal valorTotal;
}
