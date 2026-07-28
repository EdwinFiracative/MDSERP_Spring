package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.TbPedidos;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.TbPedidosRepository;
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
