package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.apolloframework.query.FilterBuilder.PathResolver;

/**
 * Implementation of {@link FilterCriteria} for unary filters
 * such as {@link FilterOperation#NULL} and {@link FilterOperation#NOT_NULL}
 * @author amarenco
 *
 */
public class UnaryFilterCriteria extends BaseFilterCriteria {
    
    /**
     * Default constructor
     * @param joinName the join name
     * @param fieldName the field name
     * @param operation the operation for the filter
     */
    public UnaryFilterCriteria(String joinName, String fieldName, FilterOperation operation) {
        super(joinName, fieldName, operation);
    }
    
    
    
    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        Predicate predicate = null;
        Path<?> path = pathResolver.findPath(this);
        
        if(this.operation.equals(FilterOperation.NULL)) {
            predicate = criteriaBuilder.isNull(path);
        } else if(this.operation.equals(FilterOperation.NOT_NULL)) {
            predicate = criteriaBuilder.isNotNull(path);
        } else {
            throw new IllegalArgumentException("Incorrect filter operation");
        }
        
        return predicate;
    }
}