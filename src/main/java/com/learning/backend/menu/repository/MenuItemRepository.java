package com.learning.backend.menu.repository;

import com.learning.backend.menu.entity.MenuItem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Tầng giao tiếp với bảng "menu_items" trong Database.
 * Tham khảo file mẫu CategoryRepository.java cùng thư mục.
 *
 * - JpaRepository<MenuItem, Long>: Cho sẵn save(), findById(), findAll(),
 * count()...
 * - JpaSpecificationExecutor<MenuItem>: Mở khóa tìm kiếm lọc động nhiều điều
 * kiện (Bước sau).
 */
public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long>, JpaSpecificationExecutor<MenuItem> {

    // TODO 1 (Derived Query kiểm tra trùng mã món):
    // Khai báo 1 hàm trả về `boolean` để kiểm tra xem đã có món nào trùng `code`
    // (không phân biệt hoa/thường)
    // và chưa bị xóa mềm (`deleted = false`) hay chưa.
    // Gợi ý ghép từ khóa JPA: existsBy + Code + IgnoreCase + And + DeletedFalse
    // Tham số truyền vào: (String code)

    boolean existsByCodeIgnoreCaseAndDeletedFalse(String code);

    // TODO 2 (Optional — dùng khi tìm chi tiết 1 món chưa xóa):
    // Khai báo hàm trả về `Optional<MenuItem>` tên `findByIdAndDeletedFalse(Long
    // id)`
    // (Nhớ import java.util.Optional;)
    Optional<MenuItem> findByIdAndDeletedFalse(Long id);

    /**
     * BÀI TẬP NẤC 2.2 (Phần 1/3 — Derived Query lọc theo Khóa ngoại Category):
     * - Ngữ cảnh App Mobile: Khách bấm tab "Món chính" (id = 2) trên thanh danh
     * mục.
     * - Yêu cầu cụ thể: Khai báo hàm tìm tất cả `MenuItem` có `category.id` khớp
     * `categoryId` và `deleted = false`.
     * - Ví dụ luồng SQL: Gọi hàm với `2L` -> SQL `SELECT * FROM menu_items WHERE
     * category_id = 2 AND deleted = false`.
     * - Công cụ / Cú pháp: `List<MenuItem> findByCategoryIdAndDeletedFalse(Long
     * categoryId);`
     */
    // TODO: Viết chữ ký hàm `findByCategoryIdAndDeletedFalse` tại đây
    List<MenuItem> findByCategoryIdAndDeletedFalse(Long categoryId);

}
