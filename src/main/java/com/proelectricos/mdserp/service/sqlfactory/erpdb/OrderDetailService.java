package com.proelectricos.mdserp.service.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailNoteDto;
import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.OrderDetailReferenceDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.*;
import com.proelectricos.mdserp.repository.sqlfactory.erpdb.OrderHeaderRepository;
import com.proelectricos.mdserp.repository.sqlfactory.erpdb.OrderNoteRepository;
import com.proelectricos.mdserp.repository.sqlfactory.erpdb.OrderReferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderDetailService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderReferenceRepository orderReferenceRepository;
    private final OrderNoteRepository orderNoteRepository;

    public Optional<OrderDetailDto> findByOrderNumber(Integer orderNumber) {
        return orderHeaderRepository.findDetailByOrderHeaderNumber(orderNumber)
                .map(header -> toDto(header,
                        orderReferenceRepository.findDetailByOrderHeaderId(header.getId()),
                        orderNoteRepository.findByOrderNoteOrderHeader_IdOrderByOrderNotePosition(header.getId())));
    }

    // Pedidos con fecha dentro de los ultimos "days" dias; lineas y notas se cargan en bloque para toda la pagina
    public List<OrderDetailDto> findLastDays(int days, Pageable pageable) {
        List<OrderHeader> headers = orderHeaderRepository
                .findDetailByOrderHeaderDateFrom(LocalDate.now().minusDays(days), pageable)
                .getContent();
        if (headers.isEmpty()) {
            return List.of();
        }
        List<Long> ids = headers.stream().map(OrderHeader::getId).toList();
        Map<Long, List<OrderReference>> linesByHeader = orderReferenceRepository.findDetailByOrderHeaderIdIn(ids).stream()
                .collect(Collectors.groupingBy(line -> line.getOrderReferOrderHeader().getId()));
        Map<Long, List<OrderNote>> notesByHeader = orderNoteRepository.findByOrderNoteOrderHeader_IdInOrderByOrderNotePosition(ids).stream()
                .collect(Collectors.groupingBy(note -> note.getOrderNoteOrderHeader().getId()));

        return headers.stream()
                .map(header -> toDto(header,
                        linesByHeader.getOrDefault(header.getId(), List.of()),
                        notesByHeader.getOrDefault(header.getId(), List.of())))
                .toList();
    }

    private OrderDetailDto toDto(OrderHeader header, List<OrderReference> lines, List<OrderNote> orderNotes) {
        Branch branch = header.getOrderHeaderBranch();
        ThirdParty client = branch.getBranchClient().getClientThirdParty();
        Vendor vendor = header.getOrderHeaderVendor() != null ? header.getOrderHeaderVendor() : branch.getBranchVendor();

        OrderDetailDto dto = new OrderDetailDto();
        dto.setPedido(header.getOrderHeaderNumber());
        dto.setFecha(header.getOrderHeaderDate());
        dto.setOrdenCliente(header.getOrderHeaderClientOrder());
        dto.setSede(branch.getBranchCode().stripTrailing());
        dto.setCliente(client.getThirdPartyName());
        dto.setDireccion(branch.getBranchAddress());
        dto.setCiudad(branch.getBranchCity());
        dto.setNit(nit(client));
        dto.setCondicionCliente(branch.getBranchClient().getClientCrediCondition());
        dto.setCondicionPagoPedido(header.getOrderHeaderPaymeConditions());
        if (vendor != null) {
            dto.setCodigoVendedor(vendor.getVendorCode());
            dto.setVendedor(vendor.getVendorThirdParty().getThirdPartyName());
        }
        dto.setDescripcion(header.getOrderHeaderDescription());

        dto.setReferencias(lines.stream().map(this::toReferenceDto).toList());

        List<OrderDetailNoteDto> notes = orderNotes.stream().map(this::toNoteDto).toList();
        dto.setNotas(notes);
        dto.setNotasUnificadas(notes.isEmpty() ? null
                : notes.stream().map(OrderDetailNoteDto::getNota).collect(Collectors.joining(" ")));
        return dto;
    }

    private OrderDetailReferenceDto toReferenceDto(OrderReference line) {
        Reference reference = line.getOrderReferReference();
        OrderDetailReferenceDto dto = new OrderDetailReferenceDto();
        dto.setItem(line.getOrderReferPosition());
        dto.setCodigoReferencia(reference.getReferCod());
        dto.setNombre(reference.getReferName());
        dto.setUd(reference.getReferMeasuUnit().getMeasuUnitCode());
        dto.setCantidad(line.getOrderReferQuantity());
        dto.setValorUnitario(line.getOrderReferUnitPrice());
        dto.setValorTotal(line.getOrderReferUnitPrice().multiply(BigDecimal.valueOf(line.getOrderReferQuantity())));
        return dto;
    }

    private OrderDetailNoteDto toNoteDto(OrderNote note) {
        OrderDetailNoteDto dto = new OrderDetailNoteDto();
        dto.setPosicion(note.getOrderNotePosition());
        dto.setNota(note.getOrderNoteText());
        return dto;
    }

    private static String nit(ThirdParty thirdParty) {
        Short digit = thirdParty.getThirdPartyVerifDigit();
        return thirdParty.getThirdPartyIdentNumber() + (digit != null ? "-" + digit : "");
    }
}
