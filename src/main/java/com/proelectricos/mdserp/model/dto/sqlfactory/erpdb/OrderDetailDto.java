package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Detalle de un pedido de ErpDb: encabezado, sede, cliente, vendedor, referencias y notas.
 * Construido desde {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.OrderHeader}.
 */
@Getter
@Setter
public class OrderDetailDto implements Serializable {
    Long id;
    Integer pedido;
    LocalDate fecha;
    String ordenCliente;
    String proyecto;
    String condicionPagoPedido;
    String descripcion;
    BranchDto sede;
    ClientDto cliente;
    /** Vendedor del pedido; si no tiene, el de la sede. */
    VendorDto vendedor;
    List<OrderDetailReferenceDto> referencias = new ArrayList<>();
    List<OrderDetailNoteDto> notas = new ArrayList<>();
}
