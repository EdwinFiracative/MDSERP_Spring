package com.proelectricos.mdserp.service.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.ViewErpPedidoHeader;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.ViewPedidoCuentaCobroRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_fact.ViewErpPedidoHeaderSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ViewPedidoCuentaCobroService {

    private final ViewPedidoCuentaCobroRepository repository;

    public List<ViewErpPedidoHeader> findAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public List<ViewErpPedidoHeader> findAll(Pageable pageable, PedidoFilterRequest filter) {
        return repository.findAll(ViewErpPedidoHeaderSpecifications.withFilter(filter), pageable).getContent();
    }

    public Optional<ViewErpPedidoHeader> findById(String num) {
        return repository.findById(num);
    }
}
