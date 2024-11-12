package com.group01.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String error;

    // Constructors
    // public ApiResponse() {
    // }

    // Success response constructor
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    // Error response constructor
    public ApiResponse(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.data = null;
        this.error = error;
    }
}
