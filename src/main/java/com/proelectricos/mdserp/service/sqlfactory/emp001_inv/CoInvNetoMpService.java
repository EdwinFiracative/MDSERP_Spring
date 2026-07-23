package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv.CoInvNetoMpDto;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoMp;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoSql;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoInvNetoMpRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoInvNetoSqlRepository;
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
