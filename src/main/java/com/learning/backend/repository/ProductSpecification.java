package com.learning.backend.repository;

/**
 * GIAI ĐOẠN 2 (Dynamic Filter với JpaSpecificationExecutor):
 * TODO: Thay thế chuỗi if/else trong ProductServiceImpl bằng Specification<Product>.
 * Hỗ trợ lọc động kết hợp nhiều điều kiện (điều kiện nào null thì bỏ qua):
 * - keyword (tìm trên nameNormalized)
 * - category (tìm trên categoryNormalized)
 * - minPrice / maxPrice
 * - inStockOnly (stockQuantity > 0)
 * - hasDiscount (discountPercent > 0)
 */
public final class ProductSpecification {

    private ProductSpecification() {
    }
}
