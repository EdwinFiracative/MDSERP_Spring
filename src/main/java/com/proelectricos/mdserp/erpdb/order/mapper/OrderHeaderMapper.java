package com.proelectricos.mdserp.erpdb.order.mapper;

import com.proelectricos.mdserp.erpdb.branch.Branch;
import com.proelectricos.mdserp.erpdb.branch.dto.BranchDto;
import com.proelectricos.mdserp.erpdb.client.Client;
import com.proelectricos.mdserp.erpdb.client.dto.ClientDto;
import com.proelectricos.mdserp.erpdb.measurunit.MeasurUnit;
import com.proelectricos.mdserp.erpdb.measurunit.dto.MeasurUnitDto;
import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import com.proelectricos.mdserp.erpdb.order.OrderNote;
import com.proelectricos.mdserp.erpdb.order.OrderReferStatus;
import com.proelectricos.mdserp.erpdb.order.OrderReference;
import com.proelectricos.mdserp.erpdb.order.dto.OrderHeaderDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailNoteDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderDetailReferenceDto;
import com.proelectricos.mdserp.erpdb.order.dto.OrderReferStatusDto;
import com.proelectricos.mdserp.erpdb.project.Project;
import com.proelectricos.mdserp.erpdb.project.dto.ProjectDto;
import com.proelectricos.mdserp.erpdb.reference.Reference;
import com.proelectricos.mdserp.erpdb.reference.dto.ReferenceDto;
import com.proelectricos.mdserp.erpdb.thirdparty.ThirdParty;
import com.proelectricos.mdserp.erpdb.thirdparty.dto.ThirdPartyDto;
import com.proelectricos.mdserp.erpdb.vendor.Vendor;
import com.proelectricos.mdserp.erpdb.vendor.dto.VendorDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapea el pedido de ErpDb (OrderHeader + lineas + notas) a {@link OrderHeaderDto} con ModelMapper.
 * Los DTO usan los nombres de columna de la base; solo los id se mapean explicitamente
 * (en la entidad se llaman "id"). Las reglas son STRICT y solo aplican a estos tipos,
 * sin alterar la configuracion global del bean.
 */
@Component
public class OrderHeaderMapper {

    private final ModelMapper modelMapper;

    public OrderHeaderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        Configuration strict = modelMapper.getConfiguration().copy()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        // branchCode es NCHAR(2): se quitan los espacios de relleno
        Converter<String, String> stripTrailing = ctx -> ctx.getSource() == null ? null : ctx.getSource().stripTrailing();

        // Primero los tipos anidados, para que los mapas padre los reutilicen
        List<TypeMap<?, ?>> typeMaps = List.of(
                modelMapper.createTypeMap(ThirdParty.class, ThirdPartyDto.class, strict)
                        .addMapping(ThirdParty::getId, ThirdPartyDto::setThirdPartyId),
                modelMapper.createTypeMap(Client.class, ClientDto.class, strict)
                        .addMapping(Client::getId, ClientDto::setClientId),
                modelMapper.createTypeMap(Vendor.class, VendorDto.class, strict)
                        .addMapping(Vendor::getId, VendorDto::setVendorId),
                modelMapper.createTypeMap(Branch.class, BranchDto.class, strict)
                        .addMapping(Branch::getId, BranchDto::setBranchId)
                        .addMappings(m -> m.using(stripTrailing).map(Branch::getBranchCode, BranchDto::setBranchCode)),
                modelMapper.createTypeMap(MeasurUnit.class, MeasurUnitDto.class, strict)
                        .addMapping(MeasurUnit::getId, MeasurUnitDto::setMeasuUnitId),
                modelMapper.createTypeMap(Reference.class, ReferenceDto.class, strict)
                        .addMapping(Reference::getId, ReferenceDto::setReferId),
                modelMapper.createTypeMap(Project.class, ProjectDto.class, strict)
                        .addMapping(Project::getId, ProjectDto::setProjeId),
                modelMapper.createTypeMap(OrderReferStatus.class, OrderReferStatusDto.class, strict)
                        .addMapping(OrderReferStatus::getId, OrderReferStatusDto::setOrderReferStatusId),
                modelMapper.createTypeMap(OrderReference.class, OrderDetailReferenceDto.class, strict)
                        .addMapping(OrderReference::getId, OrderDetailReferenceDto::setOrderReferId)
                        .addMappings(m -> m.skip(OrderDetailReferenceDto::setValorTotal)),
                modelMapper.createTypeMap(OrderNote.class, OrderDetailNoteDto.class, strict)
                        .addMapping(OrderNote::getId, OrderDetailNoteDto::setOrderNoteId),
                // Lineas y notas vienen de consultas aparte; el vendedor tiene respaldo en la sede
                modelMapper.createTypeMap(OrderHeader.class, OrderHeaderDto.class, strict)
                        .addMapping(OrderHeader::getId, OrderHeaderDto::setOrderHeaderId)
                        .addMappings(m -> {
                            m.skip(OrderHeaderDto::setOrderReference);
                            m.skip(OrderHeaderDto::setOrderNote);
                        }));

        // Falla al arrancar si algun campo de estos DTO queda sin mapear
        typeMaps.forEach(TypeMap::validate);
    }

    public OrderHeaderDto toDto(OrderHeader header, List<OrderReference> lines, List<OrderNote> notes) {
        OrderHeaderDto dto = modelMapper.map(header, OrderHeaderDto.class);

        Vendor branchVendor = header.getOrderHeaderBranch().getBranchVendor();
        if (dto.getOrderHeaderVendor() == null && branchVendor != null) {
            dto.setOrderHeaderVendor(modelMapper.map(branchVendor, VendorDto.class));
        }

        dto.setOrderReference(lines.stream().map(this::toReferenceDto).toList());
        dto.setOrderNote(notes.stream().map(note -> modelMapper.map(note, OrderDetailNoteDto.class)).toList());
        return dto;
    }

    private OrderDetailReferenceDto toReferenceDto(OrderReference line) {
        OrderDetailReferenceDto dto = modelMapper.map(line, OrderDetailReferenceDto.class);
        dto.setValorTotal(line.getOrderReferUnitPrice().multiply(BigDecimal.valueOf(line.getOrderReferQuantity())));
        return dto;
    }
}
