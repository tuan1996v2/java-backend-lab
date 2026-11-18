package com.learning.backend.entity;

import com.learning.backend.util.TextNormalizer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity ánh xạ trực tiếp với bảng "products" trong cơ sở dữ liệu.
 * Trong JPA/Hibernate: Mỗi object Product đại diện cho 1 dòng (row) trong bảng.
 */
@Entity
@Builder
@Setter
@AllArgsConstructor
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = { "id", "sku", "name" })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku; // Mã sản phẩm (ví dụ: SP-001)

    @Column(nullable = false, length = 255)
    private String name; // Tên sản phẩm

    @Column(name = "name_normalized", length = 255)
    private String nameNormalized; // Tên đã chuẩn hóa (không dấu, chữ thường) để tìm kiếm nhanh

    @Column(name = "category_normalized", length = 100)
    private String categoryNormalized; // Tên đã chuẩn hóa (không dấu, chữ thường) để tìm kiếm nhanh

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price; // Giá bán

    @Column(nullable = false)
    private Integer stockQuantity; // Số lượng tồn kho

    @Column(length = 100)
    private String category; // Ngành hàng / Danh mục (Sữa tươi, Nước ngọt, Gia vị...)

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @Column(name = "discount_percent", nullable = false)
    @Builder.Default
    private Integer discountPercent = 0;

    @PrePersist
    protected void onCreate() {
        this.nameNormalized = TextNormalizer.normalizeForSearch(this.name);
        this.categoryNormalized = TextNormalizer.normalizeForSearch(this.category);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.nameNormalized = TextNormalizer.normalizeForSearch(this.name);
        this.categoryNormalized = TextNormalizer.normalizeForSearch(this.category);
        this.updatedAt = LocalDateTime.now();
    }

}