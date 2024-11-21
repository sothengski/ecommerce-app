package com.group01.ecommerce_app.loader;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLoader {

    // Generic method to load data from a JSON file
    public static <T> List<T> loadFromJson(String fileName, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = JsonLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException(fileName + " file not found in resources folder.");
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error loading " + fileName + ": " + e.getMessage(), e);
        }
    }
}
