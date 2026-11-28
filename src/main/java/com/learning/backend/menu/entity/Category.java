package com.learning.backend.menu.entity;

import com.learning.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bảng "categories" (Danh mục món ăn: Khai vị, Lẩu, Đồ uống...).
 *
 * - @Entity: Báo Hibernate class này là 1 BẢNG trong Database (tưởng tượng như 1 Sheet Excel).
 * - @Table(name = "categories"): Đặt tên bảng SQL là "categories".
 * - extends BaseEntity: Tự động có sẵn 5 cột chung (id, branch_id, created_at, updated_at, deleted).
 * - Lombok (@Getter @Setter ...): Tự sinh getter, setter, constructor, builder (KHÔNG dùng @Data trên @Entity!).
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    /** Tên danh mục hiển thị trên App Khách (vd: "Khai vị", "Lẩu & Nướng") */
    @Column(nullable = false, length = 100)
    private String name;

    /** Thứ tự sắp xếp trên màn hình (số nhỏ hiện trước). @Builder.Default giữ giá trị = 0 khi dùng .builder() */
    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    /** Đang bật hiển thị cho khách thấy hay tạm ẩn */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
