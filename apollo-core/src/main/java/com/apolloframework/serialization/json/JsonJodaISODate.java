package com.apolloframework.serialization.json;

import java.io.IOException;

import org.joda.time.LocalDate;
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
 * Provides the {@link JsonSerializer} and {@link JsonDeserializer} for {@link LocalDate}
 * objects using the {@link ISODateTimeFormat} formatter
 * @author amarenco
 *
 */
public class JsonJodaISODate {
    
    /** Default formatter for serialization/deserialization */
    private static final DateTimeFormatter FORMATTER = ISODateTimeFormat.date();
    
    /**
     * Implements the {@link JsonSerializer} for {@link LocalDate} objects
     * @author amarenco
     *
     */
    public static class Serializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            
            gen.writeString(FORMATTER.print(value));
        }
    }
    
    
    /**
     * Implements the {@link JsonDeserializer} for {@link LocalDate} objects
     * @author amarenco
     *
     */
    public static class Deserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return LocalDate.parse(p.getValueAsString(), FORMATTER);
        }
    }
}
