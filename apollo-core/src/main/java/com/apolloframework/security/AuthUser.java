package com.apolloframework.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Extension of the {@link User} object as specified in the spring core, adding the user id
 *
 */
public class AuthUser extends User {
    private static final long serialVersionUID = 201608102259L;
    protected long id;
    
    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     */
    public AuthUser(long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this(id, username, password, true, true, true, true, authorities);
    }

    
    /**
     * Construct the <code>User</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     *
     * @param id the user's id
     * @param username the username presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     * expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     *
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public AuthUser(long id, String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
    
    /**
     * Returns the user id as stored in the database
     * @return the user id
     */
    public long getId() {
        return this.id;
    }
    
    
    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }
}
