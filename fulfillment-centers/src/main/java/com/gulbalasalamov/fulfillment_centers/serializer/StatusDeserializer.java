package com.gulbalasalamov.fulfillment_centers.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;

import java.io.IOException;
import java.security.InvalidParameterException;

public class StatusDeserializer extends JsonDeserializer<Status> {
    @Override
    public Status deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText().toUpperCase();
        if (value == null || value.isEmpty()) {
            throw new InvalidFormatException(jsonParser, "Status value cannot be null or empty", value, Status.class);
        }
        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid status value: '%s'. Accepted values are: SELLABLE, UNFULFILLABLE, INBOUND.", value);
//throw new IllegalArgumentException(message);
            throw new InvalidFormatException(jsonParser,message,value,Status.class);
        }
    }
}
