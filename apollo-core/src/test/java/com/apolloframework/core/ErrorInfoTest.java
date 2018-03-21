package com.apolloframework.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import com.apolloframework.core.ErrorInfo.ErrorBlock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for the {@link ErrorInfo}
 * @author amarenco
 *
 */
public class ErrorInfoTest {

    /**
     * Tests an error message with no details
     */
    @Test
    public void testNoDetails() {
        ErrorInfo errorInfo = new ErrorInfo.Builder(HttpStatus.OK).build();

        assertEquals(HttpStatus.OK.value(), errorInfo.getStatus());
        assertEquals(HttpStatus.OK.getReasonPhrase(), errorInfo.getReason());
        assertTrue(errorInfo.getDetails().isEmpty());
    }

    
    

    /**
     * Tests an error message with text details
     */
    @Test
    public void testTextDetails() {
        ErrorInfo errorInfo = new ErrorInfo.Builder(HttpStatus.OK)
                .addDetail(1, "Text details")
                .addDetail(2, "Message: {0}{1}", "value", 1)
                .addDetail("No code")
                .build();

        assertEquals(HttpStatus.OK.value(), errorInfo.getStatus());
        assertEquals(HttpStatus.OK.getReasonPhrase(), errorInfo.getReason());

        assertEquals(3, errorInfo.getDetails().size());
        
        // Validate the details
        Optional<ErrorBlock> error;
        error = errorInfo.getDetails().stream().filter(x -> x.getCode() == 1).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Text details", error.get().getMessage());

        error = errorInfo.getDetails().stream().filter(x -> x.getCode() == 2).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Message: value1", error.get().getMessage());

        error = errorInfo.getDetails().stream().filter(x -> !x.hasCode()).findFirst();
        assertTrue(error.isPresent());
        assertEquals("No code", error.get().getMessage());
        assertNull(error.get().getCode());
    }

    
    

    /**
     * Tests an error message with {@link ObjectError} details
     */
    @Test
    public void testObjectErrorDetails() {
    	List<ObjectError> objectErrors = new ArrayList<>();
    	objectErrors.add(new ObjectError("field1", new String[] { "Object details" }, null, "Object details"));
    	objectErrors.add(new ObjectError("field2", new String[] { "Message: {0}{1}" }, new Object[] { "value", 1 }, "Message: {0}{1}"));
    	
        ErrorInfo errorInfo = new ErrorInfo.Builder(HttpStatus.OK)
                .addDetail(objectErrors)
                .build();

        assertEquals(HttpStatus.OK.value(), errorInfo.getStatus());
        assertEquals(HttpStatus.OK.getReasonPhrase(), errorInfo.getReason());

        assertEquals(2, errorInfo.getDetails().size());
        
        // Validate the details
        Optional<ErrorBlock> error;
        error = errorInfo.getDetails().stream().filter(x -> x.getMessage().equals("Object details")).findFirst();
        assertTrue(error.isPresent());
        assertFalse(error.get().hasCode());

        error = errorInfo.getDetails().stream().filter(x -> x.getMessage().equals("Message: value1")).findFirst();
        assertTrue(error.isPresent());
        assertFalse(error.get().hasCode());
        
        
        // Now add an error code
        errorInfo = new ErrorInfo.Builder(HttpStatus.OK)
                .addDetail(1, objectErrors)
                .build();

        assertEquals(HttpStatus.OK.value(), errorInfo.getStatus());
        assertEquals(HttpStatus.OK.getReasonPhrase(), errorInfo.getReason());

        assertEquals(2, errorInfo.getDetails().size());
        
        // Validate the details
        error = errorInfo.getDetails().stream().filter(x -> x.getMessage().equals("Object details")).findFirst();
        assertTrue(error.isPresent());
        assertTrue(error.get().hasCode());
        assertEquals(1, error.get().getCode().intValue());

        error = errorInfo.getDetails().stream().filter(x -> x.getMessage().equals("Message: value1")).findFirst();
        assertTrue(error.isPresent());
        assertTrue(error.get().hasCode());
        assertEquals(1, error.get().getCode().intValue());
    }


    
    
    /**
     * Tests an error message with exceptions as details
     */
    @Test
    public void testExceptionDetails() {
        ErrorInfo errorInfo = new ErrorInfo.Builder(HttpStatus.INTERNAL_SERVER_ERROR)
                .addDetail(1, new IOException("Exception message"))
                .addDetail(new RuntimeException("No code"))
                .build();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorInfo.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorInfo.getReason());

        assertEquals(2, errorInfo.getDetails().size());
        
        // Validate the details
        Optional<ErrorBlock> error;
        error = errorInfo.getDetails().stream().filter(x -> x.getCode() == 1).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Exception message", error.get().getMessage());

        error = errorInfo.getDetails().stream().filter(x -> !x.hasCode()).findFirst();
        assertTrue(error.isPresent());
        assertEquals("No code", error.get().getMessage());
        assertNull(error.get().getCode());
    }
    
    
    
    
    /**
     * Tests the JSON output of an error info
     * @throws IOException
     */
    @Test
    public void testJSON() throws IOException {
        String errorJSON = new ErrorInfo.Builder(HttpStatus.INTERNAL_SERVER_ERROR)
                .addDetail(1, new IOException("Exception message"))
                .addDetail(2, "Text details")
                .addDetail(new RuntimeException("No code"))
                .buildJSON();
        
        Map<String, Object> errorMap = new ObjectMapper().readValue(errorJSON, new TypeReference<Map<String, Object>>() {});
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMap.get("status"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorMap.get("reason"));
        assertNotNull(errorMap.get("timestamp"));
        assertNotNull(errorMap.get("details"));
        
        @SuppressWarnings("unchecked")
		List<Map<String, Object>> errorDetails = ((List<Map<String, Object>>)errorMap.get("details"));
        assertEquals(3, errorDetails.size());

        // Validate the details
        Optional<Map<String, Object>> error;
        error = errorDetails.stream().filter(x -> x.get("code").equals(1)).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Exception message", error.get().get("message"));
        
        error = errorDetails.stream().filter(x -> x.get("code").equals(2)).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Text details", error.get().get("message"));
        
        error = errorDetails.stream().filter(x -> !x.containsKey("code")).findFirst();
        assertTrue(error.isPresent());
        assertEquals("No code", error.get().get("message"));
    }
    
    
    
    
    /**
     * Tests the {@link ResponseEntity} of an error info
     * @throws IOException
     */
    @Test
    public void testResponseEntity() throws IOException {
    	ResponseEntity<ErrorInfo> errorResponse = new ErrorInfo.Builder(HttpStatus.INTERNAL_SERVER_ERROR)
                .addDetail(1, new IOException("Exception message"))
                .addDetail(2, "Text details")
                .addDetail(new RuntimeException("No code"))
                .buildResponse();
    	
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getBody().getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getBody().getReason());
    	
        assertEquals(3, errorResponse.getBody().getDetails().size());

        // Validate the details
        Optional<ErrorBlock> error;
        error = errorResponse.getBody().getDetails().stream().filter(x -> x.getCode() == 1).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Exception message", error.get().getMessage());

        error = errorResponse.getBody().getDetails().stream().filter(x -> x.getCode() == 2).findFirst();
        assertTrue(error.isPresent());
        assertEquals("Text details", error.get().getMessage());

        error = errorResponse.getBody().getDetails().stream().filter(x -> !x.hasCode()).findFirst();
        assertTrue(error.isPresent());
        assertEquals("No code", error.get().getMessage());
    }
}
