package com.apolloframework.core;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Interface for enums with a code
 * @author amarenco
 *
 */
public interface CodedEnum<T> {
    /**
     * @return the code of the enum
     */
    @JsonValue
    public T getCode();
}
