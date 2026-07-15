package com.proelectricos.mdserp.service.sqlfactory.emp001_inv;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.CoInvNetoSql;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.CoInvNetoSqlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoInvNetoSqlService {

    private final CoInvNetoSqlRepository repository;

    public List<CoInvNetoSql> findAll() {
        return repository.findAll();
    }
}



