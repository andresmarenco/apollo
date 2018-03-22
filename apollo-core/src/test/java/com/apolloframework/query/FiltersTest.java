package com.apolloframework.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Tests for the {@link Filters} factory
 * @author amarenco
 *
 */
public class FiltersTest {
    
    /**
     * Validates the instances created by {@link Filters#and(FilterCriteria...)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testAnd() {
        FilterCriteria firstClause = Filters.equals("field1", 1);
        FilterCriteria secondClause = Filters.equals("field2", 2);
        FilterCriteria thirdClause = Filters.equals("field3", 3);
        
        FilterCriteria filter = Filters.and(firstClause);
        
        // Check the instance
        assertTrue(filter instanceof BooleanFilterCriteria);
        BooleanFilterCriteria andFilter = (BooleanFilterCriteria)filter;
        
        assertEquals(BooleanOperator.AND, andFilter.getOperator());
        assertEquals(1, andFilter.getFilters().size());
        assertEquals(firstClause, andFilter.getFilters().get(0));
        
        
        // Use more than one clause
        andFilter = (BooleanFilterCriteria)Filters.and(firstClause, secondClause);
        
        assertEquals(BooleanOperator.AND, andFilter.getOperator());
        assertEquals(2, andFilter.getFilters().size());
        assertEquals(firstClause, andFilter.getFilters().get(0));
        assertEquals(secondClause, andFilter.getFilters().get(1));
        
        
        // Use the method with a list
        filter = Filters.and(Arrays.asList(firstClause, secondClause, thirdClause));
        
        // Check the instance
        assertTrue(filter instanceof BooleanFilterCriteria);
        andFilter = (BooleanFilterCriteria)filter;
        
        assertEquals(BooleanOperator.AND, andFilter.getOperator());
        assertEquals(3, andFilter.getFilters().size());
        assertEquals(firstClause, andFilter.getFilters().get(0));
        assertEquals(secondClause, andFilter.getFilters().get(1));
        assertEquals(thirdClause, andFilter.getFilters().get(2));
    }

    
    

    /**
     * Validates the instances created by {@link Filters#or(FilterCriteria...)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testOr() {
        FilterCriteria firstClause = Filters.equals("field1", 1);
        FilterCriteria secondClause = Filters.equals("field2", 2);
        FilterCriteria thirdClause = Filters.equals("field3", 3);
        
        FilterCriteria filter = Filters.or(firstClause);
        
        // Check the instance
        assertTrue(filter instanceof BooleanFilterCriteria);
        BooleanFilterCriteria orFilter = (BooleanFilterCriteria)filter;
        
        assertEquals(BooleanOperator.OR, orFilter.getOperator());
        assertEquals(1, orFilter.getFilters().size());
        assertEquals(firstClause, orFilter.getFilters().get(0));
        
        
        // Use more than one clause
        orFilter = (BooleanFilterCriteria)Filters.or(firstClause, secondClause);
        
        assertEquals(BooleanOperator.OR, orFilter.getOperator());
        assertEquals(2, orFilter.getFilters().size());
        assertEquals(firstClause, orFilter.getFilters().get(0));
        assertEquals(secondClause, orFilter.getFilters().get(1));
        
        
        // Use the method with a list
        filter = Filters.or(Arrays.asList(firstClause, secondClause, thirdClause));
        
        // Check the instance
        assertTrue(filter instanceof BooleanFilterCriteria);
        orFilter = (BooleanFilterCriteria)filter;
        
        assertEquals(BooleanOperator.OR, orFilter.getOperator());
        assertEquals(3, orFilter.getFilters().size());
        assertEquals(firstClause, orFilter.getFilters().get(0));
        assertEquals(secondClause, orFilter.getFilters().get(1));
        assertEquals(thirdClause, orFilter.getFilters().get(2));
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#isNull(String)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testIsNull() {
        FilterCriteria filter = Filters.isNull("field1");
        
        // Check the instance
        assertTrue(filter instanceof UnaryFilterCriteria);
        UnaryFilterCriteria isNullFilter = (UnaryFilterCriteria)filter;
        
        assertEquals("field1", isNullFilter.getFieldName());
        assertEquals(FilterOperation.NULL, isNullFilter.getOperation());
        assertTrue(StringUtils.isBlank(isNullFilter.getJoinName()));
        
        // Use the method with the join name
        isNullFilter = (UnaryFilterCriteria)Filters.isNull("joinTable", "field2");
        
        assertEquals("field2", isNullFilter.getFieldName());
        assertEquals(FilterOperation.NULL, isNullFilter.getOperation());
        assertEquals("joinTable", isNullFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#isNotNull(String)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testIsNotNull() {
        FilterCriteria filter = Filters.isNotNull("field1");
        
        // Check the instance
        assertTrue(filter instanceof UnaryFilterCriteria);
        UnaryFilterCriteria isNullFilter = (UnaryFilterCriteria)filter;
        
        assertEquals("field1", isNullFilter.getFieldName());
        assertEquals(FilterOperation.NOT_NULL, isNullFilter.getOperation());
        assertTrue(StringUtils.isBlank(isNullFilter.getJoinName()));
        
        // Use the method with the join name
        isNullFilter = (UnaryFilterCriteria)Filters.isNotNull("joinTable", "field2");
        
        assertEquals("field2", isNullFilter.getFieldName());
        assertEquals(FilterOperation.NOT_NULL, isNullFilter.getOperation());
        assertEquals("joinTable", isNullFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#equals(String, Object)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testEquals() {
        FilterCriteria filter = Filters.equals("field1", 1);
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        BinaryFilterCriteria equalsFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", equalsFilter.getFieldName());
        assertEquals(1, equalsFilter.getValue());
        assertEquals(FilterOperation.EQUALS, equalsFilter.getOperation());
        assertTrue(StringUtils.isBlank(equalsFilter.getJoinName()));
        
        // Use the method with the join name
        equalsFilter = (BinaryFilterCriteria)Filters.equals("joinTable", "field2", 2);
        
        assertEquals("field2", equalsFilter.getFieldName());
        assertEquals(2, equalsFilter.getValue());
        assertEquals(FilterOperation.EQUALS, equalsFilter.getOperation());
        assertEquals("joinTable", equalsFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#notEquals(String, Object)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testNotEquals() {
        FilterCriteria filter = Filters.notEquals("field1", 1);
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        BinaryFilterCriteria equalsFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", equalsFilter.getFieldName());
        assertEquals(1, equalsFilter.getValue());
        assertEquals(FilterOperation.NOT_EQUALS, equalsFilter.getOperation());
        assertTrue(StringUtils.isBlank(equalsFilter.getJoinName()));
        
        // Use the method with the join name
        equalsFilter = (BinaryFilterCriteria)Filters.notEquals("joinTable", "field2", 2);
        
        assertEquals("field2", equalsFilter.getFieldName());
        assertEquals(2, equalsFilter.getValue());
        assertEquals(FilterOperation.NOT_EQUALS, equalsFilter.getOperation());
        assertEquals("joinTable", equalsFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#in(String, Object...)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testIn() {
        FilterCriteria filter = Filters.in("field1", 1);
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        BinaryFilterCriteria inFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", inFilter.getFieldName());
        assertEquals(FilterOperation.IN, inFilter.getOperation());
        assertTrue(StringUtils.isBlank(inFilter.getJoinName()));
        assertTrue(inFilter.getValue() instanceof List);
        
        // Validate the values
        Collection<?> inValues = (Collection<?>)inFilter.getValue();
        assertEquals(1, inValues.size());
        assertTrue(inValues.contains(1));
        
        
        // Use the method with the join name
        inFilter = (BinaryFilterCriteria)Filters.in("joinTable", "field2", 1, 2);
        
        assertEquals("field2", inFilter.getFieldName());
        assertEquals(FilterOperation.IN, inFilter.getOperation());
        assertEquals("joinTable", inFilter.getJoinName());
        assertTrue(inFilter.getValue() instanceof List);
        
        // Validate the values
        inValues = (Collection<?>)inFilter.getValue();
        assertEquals(2, inValues.size());
        assertTrue(inValues.contains(1));
        assertTrue(inValues.contains(2));
        
        
        // Use the methods the receive a collection is "in" values
        filter = Filters.in("field1", Arrays.asList(1));
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        inFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", inFilter.getFieldName());
        assertEquals(FilterOperation.IN, inFilter.getOperation());
        assertTrue(StringUtils.isBlank(inFilter.getJoinName()));
        assertTrue(inFilter.getValue() instanceof List);
        
        // Validate the values
        inValues = (Collection<?>)inFilter.getValue();
        assertEquals(1, inValues.size());
        assertTrue(inValues.contains(1));
        
        
        // Use the method with the join name
        inFilter = (BinaryFilterCriteria)Filters.in("joinTable", "field2", Arrays.asList(1, 2));
        
        assertEquals("field2", inFilter.getFieldName());
        assertEquals(FilterOperation.IN, inFilter.getOperation());
        assertEquals("joinTable", inFilter.getJoinName());
        assertTrue(inFilter.getValue() instanceof List);
        
        // Validate the values
        inValues = (Collection<?>)inFilter.getValue();
        assertEquals(2, inValues.size());
        assertTrue(inValues.contains(1));
        assertTrue(inValues.contains(2));
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#isTrue(String)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testIsTrue() {
        FilterCriteria filter = Filters.isTrue("field1");
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        BinaryFilterCriteria isTrueFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", isTrueFilter.getFieldName());
        assertEquals(true, isTrueFilter.getValue());
        assertEquals(FilterOperation.EQUALS, isTrueFilter.getOperation());
        assertTrue(StringUtils.isBlank(isTrueFilter.getJoinName()));
        
        // Use the method with the join name
        isTrueFilter = (BinaryFilterCriteria)Filters.isTrue("joinTable", "field2");
        
        assertEquals("field2", isTrueFilter.getFieldName());
        assertEquals(true, isTrueFilter.getValue());
        assertEquals(FilterOperation.EQUALS, isTrueFilter.getOperation());
        assertEquals("joinTable", isTrueFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#isFalse(String)}
     * and all of its overloaded method signatures
     */
    @Test
    public void testIsFalse() {
        FilterCriteria filter = Filters.isFalse("field1");
        
        // Check the instance
        assertTrue(filter instanceof BinaryFilterCriteria);
        BinaryFilterCriteria isFalseFilter = (BinaryFilterCriteria)filter;
        
        assertEquals("field1", isFalseFilter.getFieldName());
        assertEquals(false, isFalseFilter.getValue());
        assertEquals(FilterOperation.EQUALS, isFalseFilter.getOperation());
        assertTrue(StringUtils.isBlank(isFalseFilter.getJoinName()));
        
        // Use the method with the join name
        isFalseFilter = (BinaryFilterCriteria)Filters.isFalse("joinTable", "field2");
        
        assertEquals("field2", isFalseFilter.getFieldName());
        assertEquals(false, isFalseFilter.getValue());
        assertEquals(FilterOperation.EQUALS, isFalseFilter.getOperation());
        assertEquals("joinTable", isFalseFilter.getJoinName());
    }
    
    
    
    
    /**
     * Validates the instances created by {@link Filters#not()}
     * and all of its overloaded method signatures
     */
    @Test
    public void testNot() {
        FilterCriteria innerFilter = Filters.equals("field1", 1);
        FilterCriteria filter = Filters.not(innerFilter);
        
        // Check the instance
        assertTrue(filter instanceof NotFilterCriteria);
        NotFilterCriteria notFilter = (NotFilterCriteria)filter;
        
        assertEquals(innerFilter, notFilter.getFilter());
    }
}
