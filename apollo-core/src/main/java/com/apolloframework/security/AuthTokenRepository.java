package com.apolloframework.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthTokenRepository {
    
    /**
     * Determines if a token is valid
     * @param token the token to validate
     * @return <code>true</code> if the token is valid
     */
    boolean isTokenValid(String token);
    
    
    /**
     * Parses a given token into its corresponding {@link AuthUser} object
     * If the token is invalid or if it is expired, throws an exception
     * @param token the token to parse
     * @return {@link UserDetails} object extracted from the token
     * @throws AuthenticationException
     */
    UserDetails parseToken(String token) throws AuthenticationException;
    
    
    
    /**
     * Generates a token based on the given authenticated user
     * @param user the owner of the token
     * @return the token for the user
     */
    String generateToken(AuthUser user);
    
    
    
    /**
     * Clears the tokens blacklist
     */
    void clearTokensBlacklist();


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

}
