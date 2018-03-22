package com.apolloframework.query;

import static org.junit.Assert.assertEquals;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import org.junit.Test;

import com.apolloframework.query.FilterBuilder.PathResolver;

/**
 * Tests for the {@link BaseFilterCriteria}
 * @author amarenco
 *
 */
public class BaseFilterCriteriaTest {
    /** Dummy implementation of the class */
    private static final BaseFilterCriteria DUMMY_INSTANCE = new BaseFilterCriteria("joinName", "fieldName", FilterOperation.NOT_NULL) {
        @Override
        public Predicate toPredicate(CriteriaBuilder criteriaBuilder, PathResolver pathResolver) {
            throw new UnsupportedOperationException();
        }
    };

    
    /**
     * Validates the getters for the predefined fields
     */
    @Test
    public void testFields() {
        assertEquals("joinName", DUMMY_INSTANCE.getJoinName());
        assertEquals("fieldName", DUMMY_INSTANCE.getFieldName());
        assertEquals(FilterOperation.NOT_NULL, DUMMY_INSTANCE.getOperation());
    }
    
    
    
    
    /**
     * Tests the {@link BaseFilterCriteria#toBuilder()}
     */
    @Test
    public void testToBuilder() {
        FilterBuilder filterBuilder = DUMMY_INSTANCE.toBuilder();
        assertEquals(DUMMY_INSTANCE, filterBuilder.filter);
    }
}
