package com.atuantes.mentes.user.config.serialization.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlJackson2HttpMessageConverterTest {

    private YamlJackson2HttpMessageConverter converter;

    @BeforeEach
    void setUp() {
        converter = new YamlJackson2HttpMessageConverter();
    }

    @Test
    @DisplayName("When creating converter Then should support application/yaml media type")
    void whenCreatingConverter_thenShouldSupportApplicationYamlMediaType() {
        // When & Then
        assertTrue(converter.canRead(Object.class, MediaType.parseMediaType("application/yaml")));
    }

    @Test
    @DisplayName("When writing object Then should serialize to YAML format")
    void whenWritingObject_thenShouldSerializeToYamlFormat() throws IOException {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(data, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertTrue(yaml.contains("name: \"John Doe\"") || yaml.contains("name: John Doe"));
        assertTrue(yaml.contains("age: 30"));
    }

    @Test
    @DisplayName("When writing object with null fields Then should exclude null values")
    void whenWritingObjectWithNullFields_thenShouldExcludeNullValues() throws IOException {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("email", null);
        data.put("phone", "11999999999");
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(data, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertFalse(yaml.contains("email"));
        assertTrue(yaml.contains("name"));
        assertTrue(yaml.contains("phone"));
    }

    @Test
    @DisplayName("When getting object mapper Then should return YAMLMapper instance")
    void whenGettingObjectMapper_thenShouldReturnYamlMapperInstance() {
        // When
        ObjectMapper objectMapper = converter.getObjectMapper();

        // Then
        assertNotNull(objectMapper);
        assertInstanceOf(YAMLMapper.class, objectMapper);
    }

    @Test
    @DisplayName("When checking supported media types Then should include application/yaml")
    void whenCheckingSupportedMediaTypes_thenShouldIncludeApplicationYaml() {
        // When
        var supportedMediaTypes = converter.getSupportedMediaTypes();

        // Then
        assertNotNull(supportedMediaTypes);
        assertTrue(supportedMediaTypes.stream()
                .anyMatch(mediaType -> "application".equals(mediaType.getType()) 
                        && "yaml".equals(mediaType.getSubtype())));
    }

    @Test
    @DisplayName("When writing empty object Then should create valid YAML")
    void whenWritingEmptyObject_thenShouldCreateValidYaml() throws IOException {
        // Given
        Map<String, Object> emptyData = new HashMap<>();
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(emptyData, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertTrue(yaml.contains("{}") || yaml.trim().isEmpty() || yaml.contains("---"));
    }

    @Test
    @DisplayName("When writing nested object Then should serialize hierarchically")
    void whenWritingNestedObject_thenShouldSerializeHierarchically() throws IOException {
        // Given
        Map<String, Object> address = new HashMap<>();
        address.put("street", "Main St");
        address.put("number", 123);

        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("address", address);

        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(user, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertTrue(yaml.contains("name"));
        assertTrue(yaml.contains("address"));
        assertTrue(yaml.contains("street"));
        assertTrue(yaml.contains("number"));
    }

    @Test
    @DisplayName("When writing object with all null values Then should produce minimal YAML")
    void whenWritingObjectWithAllNullValues_thenShouldProduceMinimalYaml() throws IOException {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("field1", null);
        data.put("field2", null);
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(data, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertFalse(yaml.contains("field1"));
        assertFalse(yaml.contains("field2"));
    }

    @Test
    @DisplayName("When converter is created Then should not be null")
    void whenConverterIsCreated_thenShouldNotBeNull() {
        // When & Then
        assertNotNull(converter);
    }

    @Test
    @DisplayName("When writing complex object Then should handle different data types")
    void whenWritingComplexObject_thenShouldHandleDifferentDataTypes() throws IOException {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("string", "text");
        data.put("integer", 42);
        data.put("boolean", true);
        data.put("double", 3.14);
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        // When
        converter.write(data, MediaType.parseMediaType("application/yaml"), outputMessage);

        // Then
        String yaml = outputMessage.getBodyAsString();
        assertNotNull(yaml);
        assertTrue(yaml.contains("string"));
        assertTrue(yaml.contains("integer"));
        assertTrue(yaml.contains("boolean"));
        assertTrue(yaml.contains("double"));
    }
}