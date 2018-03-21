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
    Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver);
    
    /**
     * Creates a {@link FilterBuilder} based on the current criteria
     * @return a new instance of the filter builder
     */
    FilterBuilder toBuilder();
}