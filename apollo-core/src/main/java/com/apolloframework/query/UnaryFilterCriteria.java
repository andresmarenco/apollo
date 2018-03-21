package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.apolloframework.query.FilterBuilder.PathResolver;


public class UnaryFilterCriteria extends BaseFilterCriteria {
    private Object value;
    
    /**
     * Default constructor
     * @param joinName the join name
     * @param fieldName the field name
     * @param operation the operation for the filter
     * @param value the value of the unary filter
     */
    public UnaryFilterCriteria(String joinName, String fieldName, FilterOperation operation, Object value) {
        super(joinName, fieldName, operation);
        this.value = value;
    }
    
    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }
    
    
    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        Predicate predicate = null;
        Path<?> path = pathResolver.findPath(this);
        
        if(this.operation.equals(FilterOperation.EQUALS)) {
            predicate = criteriaBuilder.equal(path, value);
        } else if(this.operation.equals(FilterOperation.NOT_EQUALS)) {
            predicate = criteriaBuilder.notEqual(path, value);
        } else if(this.operation.equals(FilterOperation.IN)) {
            predicate = path.in(value);
        } else {
            throw new IllegalArgumentException("Incorrect filter operation");
        }
        
        return predicate;
    }
}