package com.proelectricos.mdserp.erpdb.referclassification.service;

import com.proelectricos.mdserp.emp001_fact.copagoscar.CoPagosCar;
import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import com.proelectricos.mdserp.erpdb.referclassification.repository.ReferClassificationRepository;

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
