package ru.practicum.main.compilation.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.main.compilation.model.Compilation;

import java.util.ArrayList;
import java.util.List;

public class CompilationSpecification implements Specification<Compilation> {
    private final CompilationCriteria criteria;
    private final List<Predicate> predicateList;

    public CompilationSpecification(final CompilationCriteria criteria) {
        this.criteria = criteria;
        this.predicateList = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(final Root<Compilation> root, final CriteriaQuery<?> query,
                                 final CriteriaBuilder criteriaBuilder) {
        if (criteria.getPinned() != null) {
            Predicate paid = criteriaBuilder.equal(root.get("pinned"), criteria.getPinned());
            this.predicateList.add(paid);
        }

        return query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])))
                .getRestriction();
    }
}
