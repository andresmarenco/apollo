package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.apolloframework.query.FilterBuilder.PathResolver;

public class BaseFilterCriteria implements FilterCriteria {
    protected String joinName;
    protected String fieldName;
    protected FilterOperation operation;
    
    /**
     * Default constructor
     * @param joinName the join name
     * @param fieldName the field name
     * @param operation the operation for the filter
     */
    public BaseFilterCriteria(String joinName, String fieldName, FilterOperation operation) {
        this.joinName = joinName;
        this.fieldName = fieldName;
        this.operation = operation;
    }


    /**
     * @return the joinName
     */
    public String getJoinName() {
        return joinName;
    }


    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     * @return the operation
     */
    public FilterOperation getOperation() {
        return operation;
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


	@Override
	public FilterBuilder toBuilder() {
		return new FilterBuilder(this);
	}
}