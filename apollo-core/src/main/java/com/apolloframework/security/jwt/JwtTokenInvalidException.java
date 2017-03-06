package com.apolloframework.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenInvalidException extends AuthenticationException {

    private static final long serialVersionUID = 201610161753L;
    
    /**
     * Constructs an {@link JwtTokenInvalidException} with the specified message and no root cause.
     * @param msg the detail message
     */
    public JwtTokenInvalidException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@link JwtTokenInvalidException} with the specified message and root cause.
     * @param msg the detail message
     * @param t the root cause
     */
    public JwtTokenInvalidException(String msg, Throwable t) {
        super(msg, t);
    }


}
