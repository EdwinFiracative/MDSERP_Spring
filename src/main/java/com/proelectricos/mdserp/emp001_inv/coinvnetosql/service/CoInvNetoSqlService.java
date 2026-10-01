package com.proelectricos.mdserp.emp001_inv.coinvnetosql.service;

import com.proelectricos.mdserp.emp001_inv.coinvnetosql.CoInvNetoSql;
import com.proelectricos.mdserp.emp001_inv.coinvnetosql.repository.CoInvNetoSqlRepository;
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



