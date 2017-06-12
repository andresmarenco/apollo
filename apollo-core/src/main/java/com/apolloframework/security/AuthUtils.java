package com.apolloframework.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    
    /**
     * Gets the user from the current security context
     * @return the current user
     */
    public static AuthUser getCurrentUser() {
        return (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
