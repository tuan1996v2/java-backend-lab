package com.learning.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Lỗi nghiệp vụ nhà hàng (ví dụ: món đã hết hàng, bàn đang có khách, đơn đã thanh toán).
 * Mặc định trả HTTP 400 BAD_REQUEST, hoặc truyền HttpStatus.CONFLICT (409) khi trùng dữ liệu.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
