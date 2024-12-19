package com.epam.wca.statistics.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IntegerKeyMapDeserializer extends JsonDeserializer<Map<Integer, Map<Integer, Integer>>> {
    @Override
    public Map<Integer, Map<Integer, Integer>> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        TreeNode treeNode = p.getCodec().readTree(p);
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
        if (treeNode instanceof ObjectNode objectNode) {
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                Integer key = Integer.valueOf(fieldName);
                Map<Integer, Integer> innerMap = p.getCodec().treeToValue(objectNode.get(fieldName), Map.class);
                result.put(key, innerMap);
            }
        }
        return result;
    }
}
