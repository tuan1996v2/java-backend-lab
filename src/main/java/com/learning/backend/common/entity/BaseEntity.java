package com.learning.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Class cha dùng chung cho mọi bảng DB trong hệ thống Nhà hàng.
 *
 * Ánh xạ sang TypeScript:
 *   interface BaseModel { id: number; branchId: number; createdAt: string; updatedAt: string; deleted: boolean; }
 *
 * - @MappedSuperclass: Không tạo bảng riêng tên "base_entity", mà copy các cột này xuống từng bảng con (categories, menu_items, orders...).
 * - branchId = 1L: Giai đoạn đầu làm 1 nhà hàng, sau mở rộng thành chuỗi 50 chi nhánh chỉ cần lọc theo branchId.
 * - deleted = false: Xóa mềm (Soft Delete) để giữ lịch sử hóa đơn cho kế toán.
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "branch_id", nullable = false)
    private Long branchId = 1L;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
