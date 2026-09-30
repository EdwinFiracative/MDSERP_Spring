package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Detalle de un pedido de ErpDb: encabezado, referencias y notas.
 * Construido desde {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderHeader}.
 */
@Getter
@Setter
public class OrderDetailDto implements Serializable {
    Integer pedido;
    LocalDate fecha;
    String ordenCliente;
    String sede;
    String cliente;
    String direccion;
    String ciudad;
    /** Numero de identificacion + "-" + digito de verificacion (si existe). */
    String nit;
    String condicionCliente;
    String condicionPagoPedido;
    /** Vendedor del pedido; si no tiene, el de la sede. */
    String codigoVendedor;
    String vendedor;
    String descripcion;
    /** Notas concatenadas con un espacio en orden de posicion. */
    String notasUnificadas;
    List<OrderDetailReferenceDto> referencias = new ArrayList<>();
    List<OrderDetailNoteDto> notas = new ArrayList<>();
}
