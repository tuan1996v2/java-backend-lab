package com.learning.backend.dto;

import com.learning.backend.entity.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * DTO trả dữ liệu về cho Client.
 * Giúp ẩn các thông tin nhạy cảm của Entity (nếu có) hoặc format dữ liệu phù
 * hợp trước khi xuất ra JSON.
 */

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductResponse {

    Long id;
    String sku;
    String name;
    BigDecimal price;
    Integer stockQuantity;
    String category;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer discountPercent;
    BigDecimal finalPrice;

    public static ProductResponse fromEntity(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(entity.getId())
                .sku(entity.getSku())
                .name(entity.getName())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .discountPercent(entity.getDiscountPercent())
                .finalPrice(calculateFinalPrice(entity.getPrice(), entity.getDiscountPercent()))
                .build();
    }

    private static BigDecimal calculateFinalPrice(BigDecimal price, Integer discountPercent) {
        if (price == null) {
            return null;
        }
        int discount = discountPercent != null ? discountPercent : 0;
        return price.multiply(BigDecimal.valueOf(100 - discount))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

}
