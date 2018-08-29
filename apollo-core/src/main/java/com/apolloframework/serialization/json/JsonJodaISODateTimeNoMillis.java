package com.apolloframework.serialization.json;

import java.io.IOException;

import org.joda.time.DateTime;
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
 * Provides the {@link JsonSerializer} and {@link JsonDeserializer} for {@link DateTime}
 * objects using the {@link ISODateTimeFormat} formatter
 * @author amarenco
 *
 */
public class JsonJodaISODateTimeNoMillis {
    
    /** Default formatter for serialization/deserialization */
    private static final DateTimeFormatter FORMATTER = ISODateTimeFormat.dateTimeNoMillis();
    
    /**
     * Implements the {@link JsonSerializer} for {@link DateTime} objects
     * @author amarenco
     *
     */
    public static class Serializer extends JsonSerializer<DateTime> {
        @Override
        public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            
            gen.writeString(FORMATTER.print(value));
        }
    }
    
    
    /**
     * Implements the {@link JsonDeserializer} for {@link LocalDateTime} objects
     * @author amarenco
     *
     */
    public static class Deserializer extends JsonDeserializer<DateTime> {
        @Override
        public DateTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return DateTime.parse(p.getValueAsString(), FORMATTER);
        }
    }
}
