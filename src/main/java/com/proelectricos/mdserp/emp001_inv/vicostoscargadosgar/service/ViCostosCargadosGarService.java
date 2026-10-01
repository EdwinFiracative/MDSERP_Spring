package com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.service;

import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.ViCostosCargadosGar;
import com.proelectricos.mdserp.emp001_inv.vicostoscargadosgar.repository.ViCostosCargadosGarRepository;
import com.proelectricos.mdserp.service.bitrix.BitrixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViCostosCargadosGarService {

    // Campo personalizado del deal en Bitrix24 donde se guarda el costo cargado de la OP
    private static final String BITRIX_CAMPO_VALOR_TOTAL = "UF_CRM_1790286445";

    private final ViCostosCargadosGarRepository repository;
    private final BitrixService bitrixService;

    @Transactional(readOnly = true)
    public List<ViCostosCargadosGar> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ViCostosCargadosGar> findByOP(Integer op) {
        return repository.findByOp(op);
    }

    /**
     * Obtiene el ValorTotal de una OP (lo mismo que devuelve GET /op/{op}) y lo envía
     * al campo UF_CRM_1790282776 del deal indicado en Bitrix24.
     *
     * @param op     número de orden de producción
     * @param dealId ID del deal (negociación) en Bitrix24 a actualizar
     * @return resumen con la OP, el deal, el valor enviado y la respuesta de Bitrix
     */
    @Transactional(readOnly = true)
    public Map<String, Object> enviarValorTotalABitrix(Integer op, Long dealId) {
        // 1. Se consulta la vista igual que en GET /op/{op}  ->  [{"ValorTotal":11778708}]
        List<ViCostosCargadosGar> registros = repository.findByOp(op);
        if (registros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron costos cargados para la OP " + op);
        }

        // 2. Se extrae el ValorTotal. Normalmente la vista devuelve una sola fila por OP;
        //    si llegara a devolver varias, se suman para no perder ningún costo.
        BigDecimal valorTotal = registros.stream()
                .map(ViCostosCargadosGar::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Se arma el mapa de campos a modificar en el deal y se llama crm.deal.update.
        //    NOTA: si en Bitrix el campo es de tipo "Dinero" (money), el formato esperado es
        //    "valor|MONEDA", p. ej. valorTotal.toPlainString() + "|COP".
        //    Si es de tipo número/cadena, basta con el valor tal cual.
        Map<String, Object> campos = Map.of(BITRIX_CAMPO_VALOR_TOTAL, valorTotal.toPlainString());
        Map<String, Object> respuestaBitrix = bitrixService.updateDeal(dealId, campos);

        // 4. Se devuelve un resumen para que quien llame sepa qué se envió
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("op", op);
        resultado.put("dealId", dealId);
        resultado.put("campo", BITRIX_CAMPO_VALOR_TOTAL);
        resultado.put("valorTotal", valorTotal);
        resultado.put("bitrix", respuestaBitrix);
        return resultado;
    }
}
