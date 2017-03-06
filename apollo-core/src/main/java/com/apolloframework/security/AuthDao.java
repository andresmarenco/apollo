package com.apolloframework.security;

public interface AuthDao {
    
    /**
     * Retrieves an user by its username.
     * @param username must not be null
     * @return the entity with the given username or <code>null</code> if none found
     */
    AuthUser findByUsername(String username);
    
    /**
     * Determines if a user has a specified permission over an object
     * @param user User to check
     * @param objectType Object to check the permission
     * @param objectRight Permission type
     * @return <code>true</code> if the user has permissions
     */
    boolean hasPermission(AuthUser user, String objectType, String objectRight);
    
    /**
     * Determines if a user has a specified permission over an object
     * @param id User's id
     * @param objectType Object to check the permission
     * @param objectRight Permission type
     * @return <code>true</code> if the user has permissions
     */
    boolean hasPermission(long id, String objectType, String objectRight);

    /**
     * Stores the details of the failed login attempt
     * @param user User of the event
     */
    void loginFailHandler(AuthUser user);
    
    /**
     * Stores the details of the failed login attempt
     * @param user User's id
     */
    void loginFailHandler(long id);
    
    /**
     * Stores the details of a successful login
     * @param user User of the event
     */
    void loginSuccessHandler(AuthUser user);
    
    /**
     * Stores the details of a successful login
     * @param user User's id
     */
    void loginSuccessHandler(long id);
}
