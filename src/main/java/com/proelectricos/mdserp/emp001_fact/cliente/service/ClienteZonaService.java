package com.proelectricos.mdserp.emp001_fact.cliente.service;

import com.proelectricos.mdserp.emp001_fact.cliente.repository.ClienteRepository;
import com.proelectricos.mdserp.emp001_fact.cliente.repository.ClienteRepository.ClienteZona;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Zona comercial de los clientes del sistema anterior (EMP001_FACT.CLIENTES), donde hay una fila por sede:
 * COD = numero de identificacion + "-" + codigo de sede (830142175-01) y NIT = numero + "-" + digito de verificacion.
 */
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClienteZonaService {

    private final ClienteRepository clienteRepository;

    public ZonaLookup loadLookup() {
        Map<String, String> byCod = new HashMap<>();
        Map<String, Map<String, Long>> countByNit = new HashMap<>();
        for (ClienteZona c : clienteRepository.findAllWithZona()) {
            String zona = c.getZona().trim();
            if (c.getCod() != null) {
                byCod.put(c.getCod().trim(), zona);
            }
            String ident = identOf(c.getNit());
            if (!ident.isEmpty()) {
                countByNit.computeIfAbsent(ident, k -> new HashMap<>()).merge(zona, 1L, Long::sum);
            }
        }
        // Por NIT: la zona mas frecuente entre sus sedes
        Map<String, String> byNit = countByNit.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().entrySet().stream()
                        .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                        .map(Map.Entry::getKey)
                        .orElseThrow()));
        return new ZonaLookup(byCod, byNit);
    }

    // "830142175-0" -> "830142175"
    private static String identOf(String nit) {
        if (nit == null) {
            return "";
        }
        int dash = nit.indexOf('-');
        return (dash < 0 ? nit : nit.substring(0, dash)).trim();
    }

    public record ZonaLookup(Map<String, String> byCod, Map<String, String> byNit) {

        /** Zona de la sede (por COD); si no cruza, la del cliente (por NIT); null si no hay. */
        public String find(Long identNumber, String branchCode) {
            if (identNumber == null) {
                return null;
            }
            String ident = identNumber.toString();
            String zona = branchCode == null ? null : byCod.get(ident + "-" + branchCode.trim());
            return zona != null ? zona : byNit.get(ident);
        }
    }
}
