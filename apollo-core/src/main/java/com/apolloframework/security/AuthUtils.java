package com.apolloframework.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    
    /**
     * Gets the user from the current security context
     * @return the current user or <code>null</code>
     */
    public static AuthUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context != null && context.getAuthentication() != null) {
            return (AuthUser)context.getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }
}
