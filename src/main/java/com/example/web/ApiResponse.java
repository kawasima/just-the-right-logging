package com.example.web;

import am.ik.yavi.core.ConstraintViolation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final List<ConstraintViolation> errors;

    private ApiResponse(boolean success, T data, List<ConstraintViolation> errors) {
        this.success = success;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> failure(List<ConstraintViolation> errors) {
        return new ApiResponse<>(false, null, errors);
    }

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("data")
    public T getData() {
        return data;
    }

    @JsonProperty("errors")
    public List<Map<String, String>> getErrors() {
        return Optional.ofNullable(errors)
                .map(list -> list.stream().map(violation -> Map.of(
                        "name", violation.name(),
                        "message", violation.message()
                )).toList())
                .orElse(null);
    }
}
