package com.learning.backend.menu.entity;

import java.math.BigDecimal;

import com.learning.backend.common.entity.BaseEntity;
import com.learning.backend.common.util.TextNormalizer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bảng "menu_items" (Món ăn trong thực đơn: Phở bò, Lẩu Thái, Trà đào...).
 * Tham khảo cách viết ở Category.java cùng thư mục.
 */
@Entity
// TODO 1: Bổ sung `indexes = { @Index(name = "idx_menu_branch_name", columnList
// = "branch_id, name_normalized") }` vào @Table
@Table(name = "menu_items", indexes = {
        @Index(name = "idx_menu_items_branch_name", columnList = "branch_id, name_normalized") })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuItem extends BaseEntity {

    // TODO 2 (Khóa ngoại sang Category):
    // Gắn @ManyToOne(fetch = FetchType.LAZY) và @JoinColumn(name = "category_id",
    // nullable = false)
    // private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "name_normalized", nullable = false, length = 200)
    private String nameNormalized;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MenuItemStatus status = MenuItemStatus.AVAILABLE;

    @PrePersist
    @PreUpdate
    protected void normalizeFields() {
        this.nameNormalized = TextNormalizer.normalizeForSearch(this.name);
    }
    // TODO 3 (Các cột dữ liệu của món ăn — nhớ dùng @Column(...) phù hợp):
    // - code (String): nullable = false, unique = true, length = 50 (vd: "MON-001")
    // - name (String): nullable = false, length = 200
    // - nameNormalized (String): name = "name_normalized", length = 200
    // - price (BigDecimal): nullable = false, precision = 12, scale = 2
    // - imageUrl (String): name = "image_url", length = 500
    // - description (String): length = 500
    // - status (MenuItemStatus): gắn @Enumerated(EnumType.STRING), @Column(nullable
    // = false, length = 20),
    // @Builder.Default private MenuItemStatus status = MenuItemStatus.AVAILABLE;

    // TODO 4 (Lifecycle Hook tự bỏ dấu tiếng Việt trước khi INSERT/UPDATE):
    // Gắn @PrePersist và @PreUpdate lên hàm `protected void normalizeFields()`
    // Gọi: this.nameNormalized = TextNormalizer.normalizeForSearch(this.name);
}
