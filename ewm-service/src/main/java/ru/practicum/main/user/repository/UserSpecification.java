package ru.practicum.main.user.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.main.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private final UserCriteria criteria;
    private final List<Predicate> predicateList;

    public UserSpecification(final UserCriteria criteria) {
        this.criteria = criteria;
        this.predicateList = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(final Root<User> root, final CriteriaQuery<?> query,
                                 final CriteriaBuilder criteriaBuilder) {
        if (criteria.getIds() != null) {
            Predicate category = root.get("id").in(criteria.getIds());
            this.predicateList.add(category);
        }
        return query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])))
                .getRestriction();
    }
}
