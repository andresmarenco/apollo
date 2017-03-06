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
        this.operator = operator;
        this.filters = new ArrayList<>(Arrays.asList(filters));
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
     * @return the operator
     */
    public BooleanOperator getOperator() {
        return operator;
    }


    @Override
    public Predicate convertToPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
        List<Predicate> innerPredicates = new ArrayList<>();
        for(FilterCriteria filter : filters) {
            innerPredicates.add(filter.convertToPredicate(criteriaBuilder, pathResolver));
        }
        
        return (this.operator == BooleanOperator.AND) ?
                criteriaBuilder.and(innerPredicates.toArray(new Predicate[0])) :
                    criteriaBuilder.or(innerPredicates.toArray(new Predicate[0]));
    }
    
    
//    private Filter[] filters;
    
    
    
    
    

//    private List<Filter> filters;
//    
//    /**
//     * Default constructor
//     * @param criteria the primary filter
//     */
//    public BooleanFilterCriteria(FilterCriteria criteria) {
//        this.filters = new ArrayList<>();
//        this.filters.add(new PrimaryFilter(criteria));
//    }
//    
//    
//    /**
//     * Adds a filter criteria with an AND statement
//     * @param criteria the filter criteria
//     * @return the current builder
//     */
//    public BooleanFilterCriteria and(FilterCriteria criteria) {
//        this.filters.add(new SecondaryFilter(criteria, BooleanOperator.AND));
//        return this;
//    }
//
//
//    /**
//     * Adds a filter criteria with an OR statement
//     * @param criteria the filter criteria
//     * @return the current builder
//     */
//    public BooleanFilterCriteria or(FilterCriteria criteria) {
//        this.filters.add(new SecondaryFilter(criteria, BooleanOperator.OR));
//        return this;
//    }
//    
//
//    @Override
//    public Predicate[] convertToPredicates(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
//        List<Predicate> predicates = new ArrayList<>();
//        
//        for(Filter filter : filters) {
//            Predicate[] innerPredicates = filter.getCriteria().convertToPredicates(criteriaBuilder, pathResolver);
//            
//            if(filter instanceof SecondaryFilter) {
//                switch(((SecondaryFilter)filter).clause) {
//                case AND: {
//                    predicates.add(criteriaBuilder.and(innerPredicates));
//                    break;
//                }
//                
//                case OR: {
//                    predicates.add(criteriaBuilder.or(innerPredicates));
//                    break;
//                }
//                }
//            } else {
//                predicates.addAll(Arrays.asList(innerPredicates));
//            }
//        }
//        
//        return predicates.toArray(new Predicate[0]);
//    }
//    
//    
//    /** Base interface to define boolean filters */
//    private static interface Filter {
//        FilterCriteria getCriteria();
//    }
//    
//    /** Primary filter, without boolean clause */
//    private static class PrimaryFilter implements Filter {
//        private FilterCriteria criteria;
//
//        /**
//         * Default constructor
//         * @param criteria the filter criteria
//         */
//        public PrimaryFilter(FilterCriteria criteria) {
//            this.criteria = criteria;
//        }
//        
//        @Override
//        public FilterCriteria getCriteria() {
//            return this.criteria;
//        }
//    }
//    
//    /** Secondary filter with its corresponding boolean clause */
//    private static class SecondaryFilter implements Filter {
//        private FilterCriteria criteria;
//        private BooleanOperator clause;
//        
//        /**
//         * Default constructor
//         * @param criteria the filter criteria
//         * @param clause the boolean clause
//         */
//        public SecondaryFilter(FilterCriteria criteria, BooleanOperator clause) {
//            this.criteria = criteria;
//            this.clause = clause;
//        }
//        
//        @Override
//        public FilterCriteria getCriteria() {
//            return this.criteria;
//        }
//    }

}
