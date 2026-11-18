package com.learning.backend.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Chuẩn hóa cấu trúc JSON trả về cho mọi API của ứng dụng (chuẩn công nghiệp).
 * Giúp Client (Flutter, React...) luôn nhận được format đồng nhất:
 * {
 * "success": true,
 * "message": "...",
 * "data": { ... },
 * "timestamp": "2026-11-11T15:30:00"
 * }
 */
@Setter
@Getter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Thành công", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

}
