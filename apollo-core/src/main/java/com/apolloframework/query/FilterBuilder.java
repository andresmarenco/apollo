package com.apolloframework.query;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

public class FilterBuilder {
    private Map<String, JoinType> joinMap;
    private FilterCriteria filter;
    
    /**
     * Default constructor
     * @param criteria the criteria for the filter
     */
    public FilterBuilder(FilterCriteria criteria) {
        this.joinMap = new HashMap<>();
        this.filter = criteria;
    }
    
    
    /**
     * Adds a clause to the current filter using {@link BooleanOperator#AND}.
     * Converts the current filter to {@link BooleanFilterCriteria} if required
     * @param criteria the criteria for the builder
     * @return the current builder
     */
    public FilterBuilder and(FilterCriteria... criteria) {
        if(this.filter instanceof BooleanFilterCriteria) {
            BooleanFilterCriteria boolFilter = (BooleanFilterCriteria)this.filter;
            if(boolFilter.getOperator() == BooleanOperator.AND) {
                boolFilter.add(criteria);
            } else {
                this.filter = Filters.and(this.filter).add(criteria);
            }
        } else {
            this.filter = Filters.and(this.filter).add(criteria);
        }
        
        return this;
    }
    
    
    /**
     * Adds a clause to the current filter using {@link BooleanOperator#OR}.
     * Converts the current filter to {@link BooleanFilterCriteria} if required
     * @param criteria the criteria for the builder
     * @return the current builder
     */
    public FilterBuilder or(FilterCriteria... criteria) {
        if(this.filter instanceof BooleanFilterCriteria) {
            BooleanFilterCriteria boolFilter = (BooleanFilterCriteria)this.filter;
            if(boolFilter.getOperator() == BooleanOperator.OR) {
                boolFilter.add(criteria);
            } else {
                this.filter = Filters.or(this.filter).add(criteria);
            }
            
        } else {
            this.filter = Filters.or(this.filter).add(criteria);
        }
        
        return this;
    }
    
    
    /**
     * Defines a join field using {@link JoinType#INNER}
     * @param joinField the join field
     * @return the current builder
     */
    public FilterBuilder defineJoin(String joinField) {
        return this.defineJoin(joinField, JoinType.INNER);
    }
    
    
    /**
     * Defines a join field and its type
     * @param joinField the join field
     * @param joinType the join type
     * @return the current builder
     */
    public FilterBuilder defineJoin(String joinField, JoinType joinType) {
        this.joinMap.put(joinField, joinType);
        return this;
    }
    
    
    
    
    /**
     * Converts the filter criteria into {@link Predicate} objects
     * @param criteriaBuilder the criteria builder
     * @param root the entity root
     * @return the {@link Predicate} object
     */
    public Predicate convertToPredicate(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return filter.convertToPredicate(criteriaBuilder, new PathResolver(root));
    }
    
    
    
    /** Delegate class to resolve paths */
    public class PathResolver {
        private Root<?> root;
        
        /**
         * Default constructor
         * @param root the root entity
         */
        public PathResolver(Root<?> root) {
            this.root = root;
        }
        
        /**
         * Finds the correct query path for the filter criteria
         * @param criteria the filter criteria
         * @return the path for the filter
         */
        public Path<?> findPath(BaseFilterCriteria criteria) {
            Path<?> path;
            if(StringUtils.isBlank(criteria.getJoinName())) {
                path = root.get(criteria.getFieldName());
            } else {
                JoinType joinType = joinMap.get(criteria.getJoinName());
                if(joinType == null) {
                    joinType = JoinType.INNER;
                    joinMap.put(criteria.getJoinName(), joinType);
                }
                
                path = root.join(criteria.getJoinName(), joinType)
                        .get(criteria.getFieldName());
            }
            return path;
        }
    }
}
