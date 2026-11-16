package com.learning.backend.repository;

import com.learning.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tầng Repository (DAO - Data Access Object) giao tiếp với Database.
 * Kế thừa JpaRepository<Entity, IdType> giúp ta có sẵn toàn bộ hàm CRUD cơ bản:
 * - findAll(), findById(id), save(entity), deleteById(id)... mà KHÔNG cần viết
 * câu SQL nào!
 * 
 * Spring Data JPA còn hỗ trợ "Derived Query Methods":
 * Tự động phân tích tên method để sinh câu lệnh SQL tương ứng!
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm sản phẩm theo mã SKU
    Optional<Product> findBySku(String sku);

    // Kiểm tra SKU đã tồn tại hay chưa
    boolean existsBySku(String sku);

    // Tìm kiếm tương đối theo tên đã chuẩn hóa (không phân biệt hoa/thường và dấu
    // tiếng Việt)
    List<Product> findByNameNormalizedContaining(String keyword);

    // Lọc theo danh mục
    List<Product> findByCategoryNormalizedContaining(String category);

    // Tìm kiếm kết hợp cả tên (đã chuẩn hóa) và danh mục
    List<Product> findByNameNormalizedContainingAndCategoryNormalizedContaining(String keyword, String category);
}