package com.proelectricos.mdserp.emp001_inv.coinvnetomp.service;

import com.proelectricos.mdserp.emp001_inv.coinvnetomp.CoInvNetoMp;
import com.proelectricos.mdserp.emp001_inv.coinvnetomp.dto.CoInvNetoMpDto;
import com.proelectricos.mdserp.emp001_inv.coinvnetomp.repository.CoInvNetoMpRepository;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.CoInvNetoSql;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.repository.CoInvNetoSqlRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
/*public class CoInvNetoMpService {

    private final CoInvNetoMpRepository repository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<CoInvNetoMpDto> findAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, CoInvNetoMpDto.class))
                .collect(Collectors.toList());
    }
}*/

public class CoInvNetoMpService {

    private final CoInvNetoMpRepository repository;

    public List<CoInvNetoMp> findAll() {
        return repository.findAll();
    }
}
