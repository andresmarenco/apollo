package com.apolloframework.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private static final long serialVersionUID = 201610151721L;
    
    private String token;
    
    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
    @Override
    public Object getCredentials() {
        return null;
    }
    
    @Override
    public Object getPrincipal() {
        return null;
    }
}
