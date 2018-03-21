package com.apolloframework.core;

import java.util.Collections;
import java.util.Map;

/**
 * Common responses for a REST service
 * @author amarenco
 *
 */
public class RestResponseUtils {
	/** Key for the acknowledged message */
	private static final String ACKNOWLEDGED_KEY = "acknowledged";
	
    
    /**
     * Creates a basic {@code acknowledged} message with <code>true</code> status
     * @return {@link Map} with the message
     */
    public static final Map<String,Boolean> acknowledgedMessage() {
        return acknowledgedMessage(true);
    }
    
    
    
    /**
     * Creates a basic {@code acknowledged} message 
     * @param aknowledged <code>true</code> if the message is true
     * @return {@link Map} with the message
     */
    public static final Map<String,Boolean> acknowledgedMessage(boolean aknowledged) {
        return Collections.singletonMap(ACKNOWLEDGED_KEY, aknowledged);
    }

}
