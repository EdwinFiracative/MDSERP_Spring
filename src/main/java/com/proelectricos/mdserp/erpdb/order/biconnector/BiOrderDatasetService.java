package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.branch.dto.BranchDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderBusinessLineDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailNoteDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailReferenceDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto;
import com.proelectricos.mdserp.erpdb.order.service.OrderHeaderService;
import com.proelectricos.mdserp.erpdb.project.dto.ProjectDto;
import com.proelectricos.mdserp.erpdb.reference.dto.ReferenceDto;
import com.proelectricos.mdserp.erpdb.thirdparty.dto.ThirdPartyDto;
import com.proelectricos.mdserp.erpdb.vendor.dto.VendorDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Aplana los pedidos de {@link OrderHeaderService#findByDateRange} (la misma fuente de GET /api/orderheader)
 * en las filas que el conector BI expone a Bitrix24. Las filas salen ordenadas por numero de pedido y posicion.
 */
@AllArgsConstructor
@Service
public class BiOrderDatasetService {

    private static final Pattern LINE_BREAK = Pattern.compile("\\s*\\R\\s*");
    private static final Pattern SPACES = Pattern.compile("\\s+");

    private final OrderHeaderService orderHeaderService;

    // Lineas de los pedidos con fecha entre from y to (ambas incluidas), sin filtrar por estado
    public List<BiOrderLineRow> findLines(LocalDate from, LocalDate to) {
        return orderHeaderService.findByDateRange(from, to, null).stream()
                .flatMap(header -> toLineRows(header).stream())
                .sorted(Comparator.comparing(BiOrderLineRow::numeroPedido).thenComparing(BiOrderLineRow::posicion))
                .toList();
    }

    // Notas de los pedidos con fecha entre from y to (ambas incluidas)
    public List<BiOrderNoteRow> findNotes(LocalDate from, LocalDate to) {
        return orderHeaderService.findByDateRange(from, to, null).stream()
                .flatMap(header -> toNoteRows(header).stream())
                .sorted(Comparator.comparing(BiOrderNoteRow::numeroPedido).thenComparing(BiOrderNoteRow::posicion))
                .toList();
    }

    static List<BiOrderLineRow> toLineRows(OrderHeaderDto header) {
        BranchDto branch = header.getOrderHeaderBranch();
        ThirdPartyDto client = branch.getBranchClient().getClientThirdParty();
        VendorDto vendor = header.getOrderHeaderVendor();
        ThirdPartyDto vendorThirdParty = vendor == null ? null : vendor.getVendorThirdParty();
        return header.getOrderReference().stream()
                .map(line -> {
                    ReferenceDto product = line.getOrderReferReference();
                    OrderBusinessLineDto businessLine = line.getOrderReferBusinessLine();
                    ProjectDto project = line.getOrderReferProject();
                    return new BiOrderLineRow(
                            line.getOrderReferId(),
                            header.getOrderHeaderNumber() + "-" + line.getOrderReferPosition(),
                            header.getOrderHeaderId(),
                            header.getOrderHeaderNumber(),
                            header.getOrderHeaderDate(),
                            header.getOrderHeaderPaymeConditions(),
                            normalizePaymentCondition(header.getOrderHeaderPaymeConditions()),
                            header.getOrderHeaderDescription(),
                            branch.getBranchId(),
                            branch.getBranchCode(),
                            branch.getBranchCity(),
                            branch.getBranchAddress(),
                            branch.getBranchZone(),
                            branch.getBranchClient().getClientId(),
                            identNumber(client),
                            client.getThirdPartyName(),
                            vendor == null ? null : vendor.getVendorId(),
                            vendor == null ? null : vendor.getVendorCode(),
                            identNumber(vendorThirdParty),
                            vendorThirdParty == null ? null : vendorThirdParty.getThirdPartyName(),
                            line.getOrderReferPosition(),
                            product.getReferId(),
                            product.getReferCod(),
                            singleLine(product.getReferName()),
                            singleLine(product.getReferDescription()),
                            product.getReferMeasuUnit() == null ? null : product.getReferMeasuUnit().getMeasuUnitName(),
                            businessLine == null ? null : businessLine.getReferClassId(),
                            businessLine == null ? null : businessLine.getReferClassName(),
                            line.getOrderReferQuantity(),
                            line.getOrderReferUnitPrice(),
                            line.getValorTotal(),
                            line.getOrderReferDelivDate(),
                            project == null ? null : project.getProjeId(),
                            project == null ? null : project.getProjeName(),
                            line.getOrderReferStatus().getOrderReferStatusId(),
                            line.getOrderReferStatus().getOrderReferStatusName());
                })
                .toList();
    }

    static List<BiOrderNoteRow> toNoteRows(OrderHeaderDto header) {
        return header.getOrderNote().stream()
                .map((OrderDetailNoteDto note) -> new BiOrderNoteRow(
                        header.getOrderHeaderNumber(),
                        header.getOrderHeaderDate(),
                        note.getOrderNoteId(),
                        note.getOrderNotePosition(),
                        note.getOrderNoteText()))
                .toList();
    }

    // Texto libre con variantes ("CREDITO  VS" = "CREDITO VS"): trim, espacios colapsados y mayusculas
    static String normalizePaymentCondition(String value) {
        return value == null ? null : SPACES.matcher(value.strip()).replaceAll(" ").toUpperCase(Locale.ROOT);
    }

    // Los nombres de producto pueden traer saltos de linea: se cambian por un espacio
    static String singleLine(String value) {
        return value == null ? null : LINE_BREAK.matcher(value).replaceAll(" ");
    }

    // NIT o cedula como texto: en BI es un identificador, no una cantidad
    private static String identNumber(ThirdPartyDto thirdParty) {
        return thirdParty == null || thirdParty.getThirdPartyIdentNumber() == null
                ? null : thirdParty.getThirdPartyIdentNumber().toString();
    }
}
