package com.apolloframework.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.apolloframework.security.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public abstract class AbstractJwtUtils {
    
    protected static Logger log = LoggerFactory.getLogger(AbstractJwtUtils.class);
    
    protected static final String CLAIM_USER_ID = "userId";
    protected static final String CLAIM_USERNAME = "username";
    protected static final String CLAIM_AUTHORITIES = "authorities";
    
    protected final String secretKey;
    protected final int expirationTime;
    
    /**
     * Default Constructor. Sets the required values for the JWT creation and parsing
     * @param secretKey the security key for all tokens
     * @param expirationTime the expiration time in minutes for the token
     */
    public AbstractJwtUtils(String secretKey, int expirationTime) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
    }
    
    
    
    /**
     * Determines if a token is valid
     * @param token the token to validate
     * @return <code>true</code> if the token is valid
     */
    public boolean isTokenValid(String token) {
        boolean valid = true;
        try
        {
            parseClaims(token);
        }
        catch(JwtException ex) {
            valid = false;
        }
        
        return valid;
    }
    
    
    /**
     * Parses a given token into its corresponding {@link AuthUser} object
     * If the token is invalid or if it is expired, throws an exception
     * @param token the JWT to parse
     * @return {@link UserDetails} object extracted from the token
     * @throws JwtTokenMalformedException
     * @throws JwtTokenExpiredException
     */
    public UserDetails parseToken(String token) throws JwtTokenMalformedException, JwtTokenExpiredException {
        UserDetails user = null;
        
        try
        {
            Claims claims = parseClaims(token);
            
            user = new AuthUser(
                    claims.get(CLAIM_USER_ID, Integer.class),
                    claims.get(CLAIM_USERNAME, String.class),
                    StringUtils.EMPTY,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(CLAIM_AUTHORITIES, String.class)));
        }
        catch(ExpiredJwtException ex) {
            throw new JwtTokenExpiredException("Token is expired", ex);
        }
        catch(JwtException ex) {
            log.error("Error parsing user's token", ex);
            throw new JwtTokenMalformedException("Error parsing user's token", ex);
        }
        
        return user;
    }
    
    
    
    
    /**
     * Generates a token based on the given authenticated user
     * @param user the owner of the token
     * @return JWT for the user
     */
    public String generateToken(AuthUser user) {
        return Jwts.builder()
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_USERNAME, user.getUsername())
                .claim(CLAIM_AUTHORITIES, StringUtils.join(user.getAuthorities(), ","))
                .setExpiration(LocalDateTime.now().plusMinutes(this.expirationTime).toDate())
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }
    
    
    
    /**
     * Parses a given token into its corresponding {@link Claims} object
     * @param token the JWT to parse
     * @return {@link Claims} object extracted from the token
     */
    protected Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
