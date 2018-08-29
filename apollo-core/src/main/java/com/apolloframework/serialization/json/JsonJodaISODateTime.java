package com.apolloframework.serialization.json;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Provides the {@link JsonSerializer} and {@link JsonDeserializer} for {@link LocalDateTime}
 * objects using the {@link ISODateTimeFormat} formatter
 * @author amarenco
 *
 */
public class JsonJodaISODateTime {
    
    /** Default formatter for serialization/deserialization */
    private static final DateTimeFormatter FORMATTER = ISODateTimeFormat.dateTime();
    
    /**
     * Implements the {@link JsonSerializer} for {@link LocalDateTime} objects
     * @author amarenco
     *
     */
    public static class Serializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            
            gen.writeString(FORMATTER.print(value));
        }
    }
    
    
    /**
     * Implements the {@link JsonDeserializer} for {@link LocalDateTime} objects
     * @author amarenco
     *
     */
    public static class Deserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return LocalDateTime.parse(p.getValueAsString(), FORMATTER);
        }
    }
}
