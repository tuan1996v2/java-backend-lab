package com.learning.backend.menu.repository;

import com.learning.backend.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Tầng giao tiếp với bảng "categories" trong Database.
 * Chỉ cần khai báo interface (không cần viết class impl!) — Spring Data JPA tự sinh code thực thi.
 *
 * - JpaRepository<Category, Long>:
 *   + Category: Entity (bảng) cần thao tác
 *   + Long: Kiểu dữ liệu của Khóa chính (id) trong BaseEntity
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Spring tự dịch tên hàm này thành câu SQL:
     *   SELECT * FROM categories
     *   WHERE branch_id = ? AND active = true AND deleted = false
     *   ORDER BY sort_order ASC
     */
    List<Category> findByBranchIdAndActiveTrueAndDeletedFalseOrderBySortOrderAsc(Long branchId);
}
