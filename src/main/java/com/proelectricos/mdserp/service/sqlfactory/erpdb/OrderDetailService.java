package com.proelectricos.mdserp.service.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.dto.sqlfactory.erpdb.*;
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
        Vendor vendor = header.getOrderHeaderVendor() != null ? header.getOrderHeaderVendor() : branch.getBranchVendor();

        OrderDetailDto dto = new OrderDetailDto();
        dto.setId(header.getId());
        dto.setPedido(header.getOrderHeaderNumber());
        dto.setFecha(header.getOrderHeaderDate());
        dto.setOrdenCliente(header.getOrderHeaderClientOrder());
        dto.setProyecto(header.getOrderHeaderProject());
        dto.setCondicionPagoPedido(header.getOrderHeaderPaymeConditions());
        dto.setDescripcion(header.getOrderHeaderDescription());
        dto.setSede(toBranchDto(branch));
        dto.setCliente(toClientDto(branch.getBranchClient()));
        dto.setVendedor(vendor != null ? toVendorDto(vendor) : null);

        dto.setReferencias(lines.stream().map(this::toReferenceLineDto).toList());

        dto.setNotas(orderNotes.stream().map(this::toNoteDto).toList());
        return dto;
    }

    private OrderDetailReferenceDto toReferenceLineDto(OrderReference line) {
        OrderDetailReferenceDto dto = new OrderDetailReferenceDto();
        dto.setId(line.getId());
        dto.setItem(line.getOrderReferPosition());
        dto.setReferencia(toReferenceDto(line.getOrderReferReference()));
        dto.setCantidad(line.getOrderReferQuantity());
        dto.setValorUnitario(line.getOrderReferUnitPrice());
        dto.setValorTotal(line.getOrderReferUnitPrice().multiply(BigDecimal.valueOf(line.getOrderReferQuantity())));
        dto.setEstadoAprobacion(line.getOrderReferApproState());
        dto.setFechaEntrega(line.getOrderReferDelivDate());
        return dto;
    }

    private OrderDetailNoteDto toNoteDto(OrderNote note) {
        OrderDetailNoteDto dto = new OrderDetailNoteDto();
        dto.setPosicion(note.getOrderNotePosition());
        dto.setNota(note.getOrderNoteText());
        return dto;
    }

    private BranchDto toBranchDto(Branch branch) {
        BranchDto dto = new BranchDto();
        dto.setId(branch.getId());
        dto.setBranchCode(branch.getBranchCode().stripTrailing());
        dto.setBranchCity(branch.getBranchCity());
        dto.setBranchAddress(branch.getBranchAddress());
        return dto;
    }

    private ClientDto toClientDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setClientThirdParty(toThirdPartyDto(client.getClientThirdParty()));
        dto.setClientClassification(client.getClientClassification());
        dto.setClientCrediLimit(client.getClientCrediLimit());
        dto.setClientCrediCondition(client.getClientCrediCondition());
        return dto;
    }

    private VendorDto toVendorDto(Vendor vendor) {
        VendorDto dto = new VendorDto();
        dto.setId(vendor.getId());
        dto.setVendorThirdParty(toThirdPartyDto(vendor.getVendorThirdParty()));
        dto.setVendorCode(vendor.getVendorCode());
        return dto;
    }

    private ThirdPartyDto toThirdPartyDto(ThirdParty thirdParty) {
        ThirdPartyDto dto = new ThirdPartyDto();
        dto.setId(thirdParty.getId());
        dto.setThirdPartyIdentNumber(thirdParty.getThirdPartyIdentNumber());
        dto.setThirdPartyVerifDigit(thirdParty.getThirdPartyVerifDigit());
        dto.setNit(nit(thirdParty));
        dto.setThirdPartyName(thirdParty.getThirdPartyName());
        dto.setThirdPartyCity(thirdParty.getThirdPartyCity());
        dto.setThirdPartyAddress(thirdParty.getThirdPartyAddress());
        dto.setThirdPartyPhoneNumber(thirdParty.getThirdPartyPhoneNumber());
        dto.setThirdPartyEmail(thirdParty.getThirdPartyEmail());
        return dto;
    }

    private ReferenceDto toReferenceDto(Reference reference) {
        ReferenceDto dto = new ReferenceDto();
        dto.setId(reference.getId());
        dto.setReferCod(reference.getReferCod());
        dto.setReferCod2(reference.getReferCod2());
        dto.setReferName(reference.getReferName());
        dto.setReferDescription(reference.getReferDescription());
        dto.setReferMeasuUnit(toMeasurUnitDto(reference.getReferMeasuUnit()));
        return dto;
    }

    private MeasurUnitDto toMeasurUnitDto(MeasurUnit unit) {
        MeasurUnitDto dto = new MeasurUnitDto();
        dto.setId(unit.getId());
        dto.setMeasuUnitCode(unit.getMeasuUnitCode());
        dto.setMeasuUnitName(unit.getMeasuUnitName());
        dto.setMeasuUnitDianCode(unit.getMeasuUnitDianCode());
        return dto;
    }

    private static String nit(ThirdParty thirdParty) {
        Short digit = thirdParty.getThirdPartyVerifDigit();
        return thirdParty.getThirdPartyIdentNumber() + (digit != null ? "-" + digit : "");
    }
}
