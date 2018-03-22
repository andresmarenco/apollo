package com.apolloframework.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import com.apolloframework.query.FilterBuilder.PathResolver;

public class BooleanFilterCriteria implements FilterCriteria {
    
    private List<FilterCriteria> filters;
    private BooleanOperator operator;
    
    /**
     * Default constructor
     * @param operator the operator to join the filters
     * @param filters the filters
     */
    public BooleanFilterCriteria(BooleanOperator operator, FilterCriteria... filters) {
        this(operator, new ArrayList<>(Arrays.asList(filters)));
    }
    
    
    /**
     * Default constructor
     * @param operator the operator to join the filters
     * @param filters the filters
     */
    public BooleanFilterCriteria(BooleanOperator operator, List<FilterCriteria> filters) {
        this.operator = operator;
        this.filters = filters;
    }
    
    
    /**
     * Adds a filter in the boolean criteria
     * @param criteria the filter criteria to add
     * @return the current criteria
     */
    public BooleanFilterCriteria add(FilterCriteria... criteria) {
        for(FilterCriteria f : criteria)
            this.filters.add(f);
        
        return this;
    }
    
    
    /**
     * @return the operator of the boolean clauses
     */
    public BooleanOperator getOperator() {
        return operator;
    }
    
    
    /**
     * @return the list of included filters
     */
    public List<FilterCriteria> getFilters() {
        return this.filters;
    }


    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        List<Predicate> innerPredicates = new ArrayList<>();
        for(FilterCriteria filter : filters) {
            innerPredicates.add(filter.toPredicate(criteriaBuilder, pathResolver));
        }
        
        return (this.operator == BooleanOperator.AND) ?
                criteriaBuilder.and(innerPredicates.toArray(new Predicate[0])) :
                    criteriaBuilder.or(innerPredicates.toArray(new Predicate[0]));
    }
    

    @Override
    public FilterBuilder toBuilder() {
        return new FilterBuilder(this);
    }

}
