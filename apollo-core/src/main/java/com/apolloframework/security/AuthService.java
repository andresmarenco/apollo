package com.apolloframework.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.apolloframework.security.AuthUser;

public interface AuthService extends UserDetailsService {
        
    /**
     * Determines if a user has a specified permission over an object
     * @param user User to check
     * @param objectType Object to check the permission
     * @param objectRight Permission type
     * @return <code>true</code> if the user has permissions
     */
    boolean hasPermission(AuthUser user, String objectType, String objectRight);
    
    /**
     * Handles the event of a failed login attempt
     * <ul>
     * <li>Logs the timestamp of the event</li>
     * <li>Increases the failed login attempts counter</li>
     * </ul>
     * @param user User id of the event
     */
    void loginFailHandler(long userId);    
    
    /**
     * Handles the event of a successful login:
     * <ul>
     * <li>Logs the timestamp of the event</li>
     * <li>Resets the failed login attempts counter</li>
     * </ul>
     * @param user User of the event
     */
    void loginSuccessHandler(AuthUser user);
    
    
    /**
     * Generates a new authentication token for the user
     * @param user the user to create the token
     * @return the generated token
     */
    String generateToken(AuthUser user);
    
    
    /**
     * Adds a token into the blacklist
     * @param token the token to add
     */
    void blacklistToken(String token);
    
    
    /**
     * Determines if a token is blacklisted
     * @param token the token to verify
     * @return <code>true</code> if the token is blacklisted
     */
    boolean isTokenBlacklisted(String token);
    
    
    /**
     * Determines if a token is valid
     * @param token the token to verify
     * @return <code>true</code> if the token is valid
     */
    boolean isTokenValid(String token);
}
