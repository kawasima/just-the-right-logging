package com.example.web;

import am.ik.yavi.core.ConstraintViolation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testSuccessResponse() throws JsonProcessingException {
        // Arrange
        String data = "Sample Data";

        // Act
        ApiResponse<String> response = ApiResponse.success(data);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(data, response.getData());
        assertNull(response.getErrors());

        // JSON serialization should not include errors when null
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(response);
        assertFalse(json.contains("errors"));
    }

    @Test
    void testFailureResponse() {
        // Arrange
        ConstraintViolation violation = ConstraintViolation.builder()
                .name("field")
                .message("error message");
        List<ConstraintViolation> errors = List.of(violation);

        // Act
        ApiResponse<Object> response = ApiResponse.failure(errors);

        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        List<Map<String, String>> errorList = response.getErrors();
        assertNotNull(errorList);
        assertEquals(1, errorList.size());
        Map<String, String> errorMap = errorList.getFirst();
        assertEquals("field", errorMap.get("name"));
        assertEquals("error message", errorMap.get("message"));
    }
}