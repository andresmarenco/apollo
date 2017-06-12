package com.apolloframework.core;

import java.util.Collections;
import java.util.Map;

public class JsonResponseUtils {
    
    /**
     * Creates a basic {@code acknowledged} message with <code>true</code> status
     * @return {@link Map} with the message
     */
    public static final Map<String,Boolean> aknowledgedMessage() {
        return aknowledgedMessage(true);
    }
    
    /**
     * Creates a basic {@code acknowledged} message 
     * @param aknowledged <code>true</code> if the message is true
     * @return {@link Map} with the message
     */
    public static final Map<String,Boolean> aknowledgedMessage(boolean aknowledged) {
        return Collections.singletonMap("acknowledged", aknowledged);
    }

}
