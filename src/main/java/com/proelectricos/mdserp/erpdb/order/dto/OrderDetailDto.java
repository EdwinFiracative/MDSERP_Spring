package com.proelectricos.mdserp.erpdb.order.dto;

import com.proelectricos.mdserp.erpdb.branch.dto.BranchDto;
import com.proelectricos.mdserp.erpdb.vendor.dto.VendorDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Detalle de un pedido de ErpDb.
 * Construido desde {@link com.proelectricos.mdserp.erpdb.order.OrderHeader}.
 */
@Getter
@Setter
public class OrderDetailDto implements Serializable {
    Long orderHeaderId;
    Integer orderHeaderNumber;
    LocalDate orderHeaderDate;
    String orderHeaderProject;
    String orderHeaderPaymeConditions;
    String orderHeaderDescription;
    BranchDto orderHeaderBranch;
    /** Vendedor del pedido; si no tiene, el de la sede. */
    VendorDto orderHeaderVendor;
    List<OrderDetailReferenceDto> orderReference = new ArrayList<>();
    List<OrderDetailNoteDto> orderNote = new ArrayList<>();
}
