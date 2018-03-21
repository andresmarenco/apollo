package com.apolloframework.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for the {@link RestResponseUtils}
 * @author amarenco
 *
 */
public class RestResponseUtilsTest {

    /**
     * Validates the serialization of the {@link RestResponseUtils#acknowledgedMessage()} and the
     * {@link RestResponseUtils#acknowledgedMessage(boolean)} methods
     * @throws IOException 
     */
    @Test
    public void testAcknowledgedMessage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writer().writeValueAsString(RestResponseUtils.acknowledgedMessage());

        // Default message on true
        Map<String, Object> messageMap = new ObjectMapper().readValue(message, new TypeReference<Map<String, Object>>() {});
        assertEquals(1, messageMap.size());
        assertTrue(messageMap.containsKey("acknowledged"));
        assertTrue((Boolean)messageMap.get("acknowledged"));

        // Message with false
        message = mapper.writer().writeValueAsString(RestResponseUtils.acknowledgedMessage(false));
        messageMap = new ObjectMapper().readValue(message, new TypeReference<Map<String, Object>>() {});
        assertEquals(1, messageMap.size());
        assertTrue(messageMap.containsKey("acknowledged"));
        assertFalse((Boolean)messageMap.get("acknowledged"));
    }
}
