package com.apolloframework.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.apolloframework.query.FilterBuilder.PathResolver;

/**
 * Implementation of {@link FilterCriteria} for unary filters
 * such as {@link FilterOperation#NULL} and {@link FilterOperation#NOT_NULL}
 * @author amarenco
 *
 */
public class TypeFilterCriteria extends BaseFilterCriteria {
    /** The type to check */
    protected Class<?> clazzType;
    
    
    /**
     * Default constructor
     * @param clazzType the type to check
     */
    public TypeFilterCriteria(Class<?> clazzType) {
        this(clazzType, false);
    }
    
    
    /**
     * Constructor with negative option
     * @param clazzType the type to check
     */
    public TypeFilterCriteria(Class<?> clazzType, boolean negative) {
        super(StringUtils.EMPTY, StringUtils.EMPTY, negative ? FilterOperation.NOT_TYPE : FilterOperation.TYPE);
        this.clazzType = clazzType;
    }
    
    
    
    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        Predicate predicate = null;
        Path<?> path = pathResolver.findPath(this);
        
        if(this.operation.equals(FilterOperation.TYPE)) {
            predicate = criteriaBuilder.equal(path.type(), clazzType);
        } else if(this.operation.equals(FilterOperation.NOT_TYPE)) {
            predicate = criteriaBuilder.notEqual(path.type(), clazzType);
        } else {
            throw new IllegalArgumentException("Incorrect filter operation");
        }
        
        return predicate;
    }
}