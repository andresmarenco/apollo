package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.apolloframework.query.FilterBuilder.PathResolver;

/**
 * A filter criteria that negates its inner filter
 * @author amarenco
 *
 */
public class NotFilterCriteria implements FilterCriteria {
    /** The filter criteria that will be negated */
    private FilterCriteria filter;
    
    
    /**
     * Default constructor
     * @param filter the filter criteria that will be negated 
     */
    public NotFilterCriteria(FilterCriteria filter) {
        this.filter = filter;
    }
    
    
    /**
     * @return the filter criteria that will be negated
     */
    public FilterCriteria getFilter() {
        return this.filter;
    }
    

    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        return criteriaBuilder.not(filter.toPredicate(criteriaBuilder, pathResolver));
    }
    

    @Override
    public FilterBuilder toBuilder() {
        return new FilterBuilder(this);
    }

}
