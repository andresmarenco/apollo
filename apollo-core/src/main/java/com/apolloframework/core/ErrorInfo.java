package com.apolloframework.core;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.joda.time.LocalDateTime;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Error message to return as the REST response
 * @author amarenco
 * 
 */
public class ErrorInfo {
	/** HTTP status of the REST response */
    private HttpStatus status;
    /** The details of the response */
    private List<ErrorBlock> details;
    /** The timestamp of the current error object */
    private LocalDateTime timestamp;
    
    
    /**
     * Default constructor
     */
    private ErrorInfo() {
    	this.details = new ArrayList<>();
    	this.timestamp = LocalDateTime.now();
    }
    
    
    /**
     * @return the HTTP status of the REST response
     */
    public int getStatus() {
        return status.value();
    }
    
    
    /**
     * @param status the HTTP status to set for the REST response
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    
    /**
     * @return the details of the response
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<ErrorBlock> getDetails() {
        return details;
    }
    
    
    /**
     * @param details the details to set
     */
    public void setDetails(List<ErrorBlock> details) {
        this.details = details;
    }
    
    
    /**
     * @return the reason phrase of the HTTP status
     */
    public String getReason() {
        return status.getReasonPhrase();
    }
    
    
    /**
     * @return the current timestamp
     */
    public String getTimestamp() {
        return this.timestamp.toString();
    }
    
    
    
    /**
     * Defines the data for every error detail
     * @author amarenco
     *
     */
    public static final class ErrorBlock {
    	/** Error code */
    	private Integer code;
    	/** Human-readable error message */
    	private String message;
    	
    	
    	/**
    	 * Default constructor
    	 */
    	public ErrorBlock() {
    	}
    	
    	
    	/**
    	 * Constructor with all fields
    	 * @param code the error code
    	 * @param message human-readable error message
    	 */
    	public ErrorBlock(int code, String message) {
    		this.code = code;
    		this.message = message;
		}


		/**
		 * @return the error code, or <code>null</code> if it has not been defined
		 */
    	@JsonInclude(JsonInclude.Include.NON_NULL)
		public Integer getCode() {
			return code;
		}
    	
    	
    	/**
    	 * @return <code>true</code> if an error code has been set
    	 */
    	@JsonIgnore
    	public boolean hasCode() {
    		return this.code != null;
    	}


		/**
		 * @return the human-readable error message
		 */
    	@JsonInclude(JsonInclude.Include.NON_NULL)
		public String getMessage() {
			return message;
		}
    }
    
    
     
    /**
     * Builder to create an {@link ErrorInfo} object or its corresponding {@link ResponseEntity}
     * @author amarenco
     *
     */
    public static final class Builder {
        private HttpStatus status;
        private List<Entry<ErrorBlock, Object[]>> details;
        private MessageSource msgSource;
        
        
        /**
         * Default Constructor
         * @param status HTTP status code
         */
        public Builder(HttpStatus status) {
            this.status = status;
            this.details = new ArrayList<>();
        }
        
        
        /**
         * Sets the default i18n message source for the error details
         * @param msgSource the i18n message source
         * @return the current builder
         */
        public Builder setMessageSource(MessageSource msgSource) {
        	this.msgSource = msgSource;
        	return this;
        }
        
        
        /**
         * Adds a detail message to the error info
         * @param detail the detail message of the error
         * @param args the arguments for the message
         * @return the current builder
         */
        public Builder addDetail(String detail, Object... args) {
        	ErrorBlock error = new ErrorBlock();
        	error.message = detail;
        	
            this.details.add(new AbstractMap.SimpleImmutableEntry<ErrorBlock, Object[]>(error, args));
            return this;
        }
        
        
        /**
         * Adds a detail message to the error info
         * @param code the code of the error
         * @param detail the detail message of the error
         * @param args the arguments for the message
         * @return the current builder
         */
        public Builder addDetail(int code, String detail, Object... args) {
        	this.details.add(new AbstractMap.SimpleImmutableEntry<ErrorBlock, Object[]>(
        			new ErrorBlock(code, detail), args));
            return this;
        }
        
        
        /**
         * Adds a list of validation errors to the details of the message
         * @param validationErrors the list of validation errors
         * @return the current builder
         */
        public Builder addDetail(List<ObjectError> validationErrors) {
            for(ObjectError error : validationErrors) {
                this.addDetail(error.getCode(), error.getArguments());
            }
            return this;
        }
        
        
        /**
         * Adds a list of validation errors to the details of the message
         * @param code the code for all the validation errors
         * @param validationErrors the list of validation errors
         * @return the current builder
         */
        public Builder addDetail(int code, List<ObjectError> validationErrors) {
            for(ObjectError error : validationErrors) {
                this.addDetail(code, error.getCode(), error.getArguments());
            }
            return this;
        }
        
        
        /**
         * Adds a detail message to the error info
         * @param cause the throwable with the message
         * @return the current builder
         */
        public Builder addDetail(Throwable cause) {
            if(cause != null)
                this.addDetail(cause.getMessage());
            
            return this;
        }
        
        
        /**
         * Adds a detail message to the error info
         * @param code the code for all the validation errors
         * @param cause the throwable with the message
         * @return the current builder
         */
        public Builder addDetail(int code, Throwable cause) {
            if(cause != null)
                this.addDetail(code, cause.getMessage());
            
            return this;
        }
        
        
        /**
         * Checks if no errors has been added to the builder
         * @return <code>true</code> if no errors has been added
         */
        public boolean noErrors() {
            return this.details.isEmpty();
        }
        
        
        /**
         * Creates an {@link ErrorInfo} object with the defined builder parameters
         * @return {@link ErrorInfo} object
         */
        public ErrorInfo build() {
        	ErrorInfo result = new ErrorInfo();
            Locale currentLocale = LocaleContextHolder.getLocale();
        	
        	// If no message source has been defined, use a default implementation 
        	if(msgSource == null) {
        		msgSource = new ResourceBundleMessageSource();
        	}
        	
        	
        	// Set the data as defined in the builder
        	result.status = this.status;
            
            for(Entry<ErrorBlock, Object[]> error : this.details) {
            	// Use the message source and the arguments to localize the message
            	error.getKey().message = msgSource.getMessage(
            			error.getKey().message,
            			error.getValue(),
            			error.getKey().message,
            			currentLocale);
            	
            	result.details.add(error.getKey());
            }
            
            return result;
        }
        
        
        
        /**
         * Creates a JSON representation of the error info message
         * @return JSON error message
         */
        public String buildJSON() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            ErrorInfo errorInfo = this.build();
            String json = "";
            
            try
            {
                json = mapper.writeValueAsString(errorInfo);
            } catch (JsonProcessingException ex) {
            	throw new IOException("Couldn't convert error message to JSON", ex);
            }
            
            return json;
        }
        
        
        /**
         * Creates a {@link ResponseEntity} object with the defined builder parameters
         * @return {@link ResponseEntity} object
         */
        public ResponseEntity<ErrorInfo> buildResponse() {
            ErrorInfo errorInfo = this.build();
            return new ResponseEntity<ErrorInfo>(errorInfo, errorInfo.status);
        }
    }
}