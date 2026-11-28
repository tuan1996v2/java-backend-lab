package com.learning.backend.common.exception;

/**
 * Lỗi 404: Không tìm thấy dữ liệu (ví dụ: món ăn, bàn ăn, đơn hàng không tồn tại).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
