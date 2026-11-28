package com.learning.backend.menu.entity;

import lombok.Getter;

/**
 * Trạng thái của món ăn trên thực đơn (App Khách & Quản lý).
 * - @Getter (Lombok): Tự động sinh hàm `getLabel()`, không cần gõ tay.
 * - Lưu trong DB dưới dạng chuỗi ("AVAILABLE", "SOLD_OUT") để nhìn bảng DB là hiểu ngay.
 */
@Getter
public enum MenuItemStatus {
    AVAILABLE("Còn món"),
    SOLD_OUT("Tạm hết món");

    private final String label;

    MenuItemStatus(String label) {
        this.label = label;
    }
}
