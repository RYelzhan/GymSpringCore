package com.epam.wca.statistics.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class IntegerKeyMapSerializer extends JsonSerializer<Map<Integer, Map<Integer, Integer>>> {
    @Override
    public void serialize(Map<Integer, Map<Integer, Integer>> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : value.entrySet()) {
            gen.writeFieldName(entry.getKey().toString());
            gen.writeObject(entry.getValue());
        }
        gen.writeEndObject();
    }
}
