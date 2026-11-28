package com.learning.backend.menu.repository;

import com.learning.backend.menu.entity.MenuItem;

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
}
