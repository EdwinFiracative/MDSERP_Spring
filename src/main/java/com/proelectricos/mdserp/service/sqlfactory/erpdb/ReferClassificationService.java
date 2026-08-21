package com.proelectricos.mdserp.service.sqlfactory.erpdb;


import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.CoPagosCar;
import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification;
import com.proelectricos.mdserp.repository.sqlfactory.erpdb.ReferClassificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReferClassificationService {

    private final ReferClassificationRepository ReferClassificationRepository;

    public List<ReferClassification> findAll() {
        return ReferClassificationRepository.findAll();
    }

}
