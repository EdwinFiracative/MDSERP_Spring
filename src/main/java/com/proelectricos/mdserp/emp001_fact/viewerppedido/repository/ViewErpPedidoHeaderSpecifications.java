package com.proelectricos.mdserp.emp001_fact.viewerppedido.repository;

import com.proelectricos.mdserp.emp001_fact.pedido.dto.PedidoFilterRequest;
import com.proelectricos.mdserp.emp001_fact.viewerppedido.ViewErpPedidoHeader;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ViewErpPedidoHeaderSpecifications {

    private ViewErpPedidoHeaderSpecifications() {
    }

    public static Specification<ViewErpPedidoHeader> withFilter(PedidoFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            addInPredicate(filter.getNum(), "num", root, predicates);
            addInPredicate(filter.getTdespacho(), "tdespacho", root, predicates);
            addInPredicate(filter.getCliente(), "cliente", root, predicates);
            addInPredicate(filter.getVendedor(), "vendedor", root, predicates);

            if (filter.getFechaAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fecha"), filter.getFechaAfter()));
            }

            if (filter.getFechaBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fecha"), filter.getFechaBefore()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addInPredicate(
            List<String> values,
            String field,
            jakarta.persistence.criteria.Root<ViewErpPedidoHeader> root,
            List<Predicate> predicates
    ) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(field).in(values));
        }
    }
}
