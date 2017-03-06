package com.apolloframework.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenExpiredException extends AuthenticationException {

    private static final long serialVersionUID = 201610161753L;
    
    /**
     * Constructs an {@link JwtTokenExpiredException} with the specified message and no root cause.
     * @param msg the detail message
     */
    public JwtTokenExpiredException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@link JwtTokenExpiredException} with the specified message and root cause.
     * @param msg the detail message
     * @param t the root cause
     */
    public JwtTokenExpiredException(String msg, Throwable t) {
        super(msg, t);
    }


}
