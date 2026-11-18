package com.learning.backend.entity;

/**
 * GIAI ĐOẠN 3 (BaseEntity + JPA Auditing + Soft Delete):
 * TODO:
 * 1. Gắn @MappedSuperclass, @Getter, @Setter, @EntityListeners(AuditingEntityListener.class)
 * 2. Đưa createdAt (@CreatedDate), updatedAt (@LastModifiedDate), deleted (boolean = false) vào đây
 * 3. Cho Product (và sau này Order, OrderItem) kế thừa BaseEntity thay vì khai báo lặp lại ở từng Entity.
 */
public abstract class BaseEntity {
}
