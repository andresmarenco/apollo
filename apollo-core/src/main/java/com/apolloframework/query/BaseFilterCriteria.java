package com.apolloframework.query;

/**
 * Base implementation of {@link FilterCriteria} with a field and an operation to be applied
 * @author amarenco
 *
 */
public abstract class BaseFilterCriteria implements FilterCriteria {
    /** The name of the foreign entity needed to apply the filter */
    protected String joinName;
    /** The name of the field in where the filter is applied */
    protected String fieldName;
    /** The operation applied to create the filter */
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
     * @return the name of the foreign entity needed to apply the filter
     */
    public String getJoinName() {
        return joinName;
    }


    /**
     * @return the name of the field in where the filter is applied
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     * @return the operation applied to create the filter
     */
    public FilterOperation getOperation() {
        return operation;
    }
    

    @Override
    public FilterBuilder toBuilder() {
        return new FilterBuilder(this);
    }

}
