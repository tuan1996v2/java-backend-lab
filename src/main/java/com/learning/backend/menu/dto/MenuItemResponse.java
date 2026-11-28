package com.learning.backend.menu.dto;

import com.learning.backend.menu.entity.MenuItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JSON trả về cho App Khách / Quản lý (không chứa cột nội bộ `nameNormalized`, `deleted`).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String code;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private MenuItemStatus status;
    private String statusLabel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
