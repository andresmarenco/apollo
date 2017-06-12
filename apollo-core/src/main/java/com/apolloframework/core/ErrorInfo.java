package com.apolloframework.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Error message to return as the REST response
 *
 */
public class ErrorInfo {
    private HttpStatus status;
    private List<String> details;
    
    /**
     * Default constructor
     */
    public ErrorInfo() {
    }
    
    /**
     * @return the status
     */
    public int getStatus() {
        return status.value();
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    /**
     * @return the details
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> getDetails() {
        return details;
    }
    
    /**
     * @param details the details to set
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }
    
    /**
     * @return the reason phrase of the HTTP status
     */
    public String getReason() {
        return status.getReasonPhrase();
    }
    
    /**
     * Current timestamp
     * @return Current timestamp
     */
    public String getTimestamp() {
        return LocalDateTime.now().toString();
    }
    
     
    /**
     * Builder to create an {@link ErrorInfo} object or its corresponding {@link ResponseEntity}
     * @author amarenco
     *
     */
    public static final class Builder {
        
        private static Logger log = LoggerFactory.getLogger(Builder.class);
        
        private HttpStatus status;
        private List<String> details;
        
        /**
         * Default Constructor
         * @param status HTTP status code
         */
        public Builder(HttpStatus status) {
            this.status = status;
            this.details = new ArrayList<>();
        }
        
        /**
         * Adds a detail message to the error info
         * @param detail Detail message of the error
         * @return Current builder
         */
        public Builder addDetail(String detail) {
            this.details.add(detail);
            return this;
        }
        
        /**
         * Adds a detail message to the error info
         * @param msgSource the message source to localize the detail
         * @param detail the detail message of the error
         * @return Current builder
         */
        public Builder addDetail(MessageSource msgSource, String detail) {
            this.addLocalizedDetail(msgSource, detail);
            return this;
        }
        
        /**
         * Adds a detail message to the error info
         * @param msgSource the message source to localize the detail
         * @param detail the detail message of the error
         * @param args the arguments for the message
         * @return Current builder
         */
        public Builder addDetail(MessageSource msgSource, String detail, Object... args) {
            this.addLocalizedDetail(msgSource, detail, args);
            return this;
        }
        
        /**
         * Adds a list of validation errors to the details of the message
         * @param validationErrors the list of validation errors
         * @return Current builder
         */
        public Builder addDetail(List<ObjectError> validationErrors) {
            for(ObjectError error : validationErrors) {
                this.details.add(error.getCode());
            }
            return this;
        }
        
        /**
         * Adds a list of validation errors to the details of the message
         * @param msgSource the message source to localize the detail
         * @param validationErrors the list of validation errors
         * @return Current builder
         */
        public Builder addDetail(MessageSource msgSource, List<ObjectError> validationErrors) {
            for(ObjectError error : validationErrors) {
                this.addLocalizedDetail(msgSource, error.getCode(), error.getArguments());
            }
            return this;
        }
        
        /**
         * Adds a detail message to the error info
         * @param cause Exception with the message
         * @return Current builder
         */
        public Builder addDetail(Throwable cause) {
            if(cause != null)
                this.details.add(cause.getMessage());
            
            return this;
        }
        
        /**
         * Adds a detail message to the error info
         * @param msgSource the message source to localize the detail
         * @param cause Exception with the message
         * @return Current builder
         */
        public Builder addDetail(MessageSource msgSource, Throwable cause) {
            if(cause != null)
                this.addLocalizedDetail(msgSource, cause.getMessage());
            
            return this;
        }
        
        
        /**
         * Adds a message using the current localization
         * @param msgSource the message bundle source
         * @param detail the detail to add
         * @param args the arguments for the message
         */
        private void addLocalizedDetail(MessageSource msgSource, String detail, Object... args) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            this.details.add(msgSource.getMessage(detail, args, detail, currentLocale));
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
            result.status = this.status;
            
            if(this.details != null && !this.details.isEmpty())
                result.details = this.details;
            
            return result;
        }
        
        
        
        /**
         * Creates a JSON representation of the error info message
         * @return JSON error message
         */
        public String buildJSON() {
            ObjectMapper mapper = new ObjectMapper();
            ErrorInfo errorInfo = this.build();
            String json = "";
            
            try
            {
                json = mapper.writeValueAsString(errorInfo);
            } catch (JsonProcessingException ex) {
                log.error("Couldn't convert error message to JSON", ex);
            }
            
            return json;
        }
        
        
        /**
         * Creates a {@link ResponseEntity} object with the defined builder parameters
         * @return {@link ResponseEntity} object
         */
        public ResponseEntity<ErrorInfo> buildResponse() {
            ErrorInfo errorInfo = this.build();
            return new ResponseEntity<ErrorInfo>(errorInfo, this.status);
        }
    }
}