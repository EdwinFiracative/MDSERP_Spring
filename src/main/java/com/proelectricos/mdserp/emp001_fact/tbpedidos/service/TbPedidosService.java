package com.proelectricos.mdserp.emp001_fact.tbpedidos.service;

import com.proelectricos.mdserp.emp001_fact.tbpedidos.TbPedidos;
import com.proelectricos.mdserp.emp001_fact.tbpedidos.repository.TbPedidosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TbPedidosService {

    private final TbPedidosRepository repository;

    @Transactional(readOnly = true)
    public Optional<TbPedidos> findByNum(String num) {
        return repository.findFirstByNum(num);
    }
}
