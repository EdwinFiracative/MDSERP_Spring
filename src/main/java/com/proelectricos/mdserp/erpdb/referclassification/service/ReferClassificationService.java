package com.proelectricos.mdserp.erpdb.referclassification.service;

import com.proelectricos.mdserp.emp001_fact.copagoscar.CoPagosCar;
import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import com.proelectricos.mdserp.erpdb.referclassification.repository.ReferClassificationRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReferClassificationService {

    private final ReferClassificationRepository ReferClassificationRepository;

    public List<ReferClassification> findAll() {
        return ReferClassificationRepository.findAll();
    }

    // Linea de negocio de cada clasificacion: su ancestro de segundo nivel (hijo directo de una raiz).
    // Las raices no tienen linea de negocio. Una sola consulta; el arbol se recorre en memoria por id.
    public Map<Long, ReferClassification> findBusinessLineByClassificationId() {
        Map<Long, ReferClassification> byId = ReferClassificationRepository.findAll().stream()
                .collect(Collectors.toMap(ReferClassification::getId, Function.identity()));
        Map<Long, ReferClassification> businessLines = new HashMap<>();
        for (ReferClassification classification : byId.values()) {
            ReferClassification node = classification;
            ReferClassification father = parentOf(node, byId);
            if (father == null) {
                continue;
            }
            while (parentOf(father, byId) != null) {
                node = father;
                father = parentOf(father, byId);
            }
            businessLines.put(classification.getId(), node);
        }
        return businessLines;
    }

    // getId() sobre el proxy LAZY no dispara consulta
    private static ReferClassification parentOf(ReferClassification node, Map<Long, ReferClassification> byId) {
        ReferClassification father = node.getReferClassFather();
        return father == null ? null : byId.get(father.getId());
    }

}
