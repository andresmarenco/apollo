package com.apolloframework.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.Predicate.BooleanOperator;

/**
 * Factory for {@link FilterCriteria} objects
 * @author amarenco
 * 
 */
public abstract class Filters {

    /**
     * Creates a {@link BooleanFilterCriteria} with the current criteria using
     * a {@link BooleanOperator#AND} operator
     * @param filters the filters to join
     * @return the boolean filter
     */
    public static BooleanFilterCriteria and(FilterCriteria... filters) {
        return new BooleanFilterCriteria(BooleanOperator.AND, filters);
    }



    /**
     * Creates a {@link BooleanFilterCriteria} with the current criteria using
     * a {@link BooleanOperator#AND} operator
     * @param filters the filters to join
     * @return the boolean filter
     */
    public static BooleanFilterCriteria and(List<FilterCriteria> filters) {
        return new BooleanFilterCriteria(BooleanOperator.AND, filters);
    }



    /**
     * Creates a {@link BooleanFilterCriteria} with the current criteria using
     * a {@link BooleanOperator#OR} operator
     * @param filters the filters to join
     * @return the boolean filter
     */
    public static BooleanFilterCriteria or(FilterCriteria... filters) {
        return new BooleanFilterCriteria(BooleanOperator.OR, filters);
    }



    /**
     * Creates a {@link BooleanFilterCriteria} with the current criteria using
     * a {@link BooleanOperator#OR} operator
     * @param filters the filters to join
     * @return the boolean filter
     */
    public static BooleanFilterCriteria or(List<FilterCriteria> filters) {
        return new BooleanFilterCriteria(BooleanOperator.OR, filters);
    }



    /**
     * Creates a <code>fieldName is null</code> filter
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isNull(String fieldName) {
        return Filters.isNull(null, fieldName);
    }



    /**
     * Creates a <code>fieldName is null</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isNull(String join, String fieldName) {
        return new UnaryFilterCriteria(join, fieldName, FilterOperation.NULL);
    }



    /**
     * Creates a <code>fieldName is not null</code> filter
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isNotNull(String fieldName) {
        return Filters.isNotNull(null, fieldName);
    }



    /**
     * Creates a <code>fieldName is not null</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isNotNull(String join, String fieldName) {
        return new UnaryFilterCriteria(join, fieldName, FilterOperation.NOT_NULL);
    }



    /**
     * Creates a <code>fieldName = value</code> filter
     * @param fieldName the name of the field
     * @param value the value of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria equals(String fieldName, Object value) {
        return Filters.equals(null, fieldName, value);
    }



    /**
     * Creates a <code>fieldName = value</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @param value the value of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria equals(String join, String fieldName, Object value) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.EQUALS, value);
    }



    /**
     * Creates a <code>fieldName != value</code> filter
     * @param fieldName the name of the field
     * @param value the value of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria notEquals(String fieldName, Object value) {
        return Filters.notEquals(null, fieldName, value);
    }



    /**
     * Creates a <code>fieldName != value</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @param value the value of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria notEquals(String join, String fieldName, Object value) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.NOT_EQUALS, value);
    }



    /**
     * Creates a <code>type(entity) = clazzType</code> filter
     * @param clazzType the type of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria type(Class<?> clazzType) {
        return new TypeFilterCriteria(clazzType);
    }



    /**
     * Creates a <code>type(entity) != clazzType</code> filter
     * @param clazzType the type of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria notType(Class<?> clazzType) {
        return new TypeFilterCriteria(clazzType, true);
    }



    /**
     * Creates a <code>fieldName in value</code> filter
     * @param fieldName the name of the field
     * @param values the values of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria in(String fieldName, Object... values) {
        return Filters.in(null, fieldName, Arrays.asList(values));
    }



    /**
     * Creates a <code>fieldName in value</code> filter
     * @param fieldName the name of the field
     * @param values the values of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria in(String fieldName, Collection<?> values) {
        return Filters.in(null, fieldName, values);
    }



    /**
     * Creates a <code>fieldName in value</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @param values the values of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria in(String join, String fieldName, Collection<?> values) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.IN, values);
    }



    /**
     * Creates a <code>fieldName in value</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @param values the values of the filter
     * @return the filter criteria object
     */
    public static FilterCriteria in(String join, String fieldName, Object... values) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.IN, Arrays.asList(values));
    }



    /**
     * Creates a <code>fieldName = true</code> filter
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isTrue(String fieldName) {
        return Filters.isTrue(null, fieldName);
    }



    /**
     * Creates a <code>fieldName = true</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isTrue(String join, String fieldName) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.EQUALS, true);
    }



    /**
     * Creates a <code>fieldName = false</code> filter
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isFalse(String fieldName) {
        return Filters.isFalse(null, fieldName);
    }



    /**
     * Creates a <code>fieldName = false</code> filter
     * @param join the name of the join
     * @param fieldName the name of the field
     * @return the filter criteria object
     */
    public static FilterCriteria isFalse(String join, String fieldName) {
        return new BinaryFilterCriteria(join, fieldName, FilterOperation.EQUALS, false);
    }




    /**
     * Creates a {@link NotFilterCriteria} with the current criteria
     * @param filter the filters to negate
     * @return the not filter
     */
    public static NotFilterCriteria not(FilterCriteria filter) {
        return new NotFilterCriteria(filter);
    }
    
}
