package com.apolloframework.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {

    private static final long serialVersionUID = 201610151719L;
    
    /**
     * Constructs an {@link JwtTokenMalformedException} with the specified message and no root cause.
     * @param msg the detail message
     */
    public JwtTokenMalformedException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@link JwtTokenMalformedException} with the specified message and root cause.
     * @param msg the detail message
     * @param t the root cause
     */
    public JwtTokenMalformedException(String msg, Throwable t) {
        super(msg, t);
    }


}
