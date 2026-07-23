package com.proelectricos.mdserp.repository.sqlfactory.emp001_fact;

import com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact.PedidoFilterRequest;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact.Pedido;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class PedidoSpecifications {

    private PedidoSpecifications() {
    }

    public static Specification<Pedido> withFilter(PedidoFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            addInPredicate(filter.getNum(), "num", root, criteriaBuilder, predicates);
            addInPredicate(filter.getTdespacho(), "tdespacho", root, criteriaBuilder, predicates);
            addInPredicate(filter.getCliente(), "cliente", root, criteriaBuilder, predicates);
            addInPredicate(filter.getCod(), "cod", root, criteriaBuilder, predicates);
            addInPredicate(filter.getVendedor(), "vendedor", root, criteriaBuilder, predicates);

            if (filter.getFechaAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fecha"), filter.getFechaAfter()));
            }

            if (filter.getFechaBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fecha"), filter.getFechaBefore()));
            }

            if (filter.getEstado() != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), filter.getEstado()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addInPredicate(
            List<String> values,
            String field,
            jakarta.persistence.criteria.Root<Pedido> root,
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(field).in(values));
        }
    }
}
