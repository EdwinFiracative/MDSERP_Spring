package com.proelectricos.mdserp.service.sqlfactory.emp001_ofer;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_ofer.VwAvioferpedidoDto;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_ofer.VwAvioferpedidoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VwAvioferpedidoService {

    private final VwAvioferpedidoRepository repository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<VwAvioferpedidoDto> findAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, VwAvioferpedidoDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VwAvioferpedidoDto> findByNUM(String NUM) {
        return repository.findByNUM(NUM).stream()
                .map(entity -> modelMapper.map(entity, VwAvioferpedidoDto.class))
                .collect(Collectors.toList());
    }
}
