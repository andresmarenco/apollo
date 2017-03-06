package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.apolloframework.query.FilterBuilder.PathResolver;

public interface FilterCriteria {
    /**
     * Converts the current criteria into the corresponding {@link Predicate} object
     * @param criteriaBuilder the criteria builder
     * @param pathResolver the delegate to resolve paths
     * @return the {@link Predicate} object
     */
    Predicate convertToPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver);
}