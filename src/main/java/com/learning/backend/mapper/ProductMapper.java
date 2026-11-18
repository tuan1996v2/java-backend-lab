package com.learning.backend.mapper;

/**
 * GIAI ĐOẠN 1 (MapStruct + Lombok):
 * TODO: Sau khi thêm dependency mapstruct + mapstruct-processor + lombok-mapstruct-binding vào pom.xml:
 * 1. Đổi class này thành interface và gắn @Mapper(componentModel = "spring")
 * 2. Khai báo các hàm map:
 *    - Product toEntity(ProductRequest request)
 *    - void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product)
 *    - ProductResponse toResponse(Product product) (tính finalPrice)
 * 3. Xóa hàm fromEntity() thủ công khỏi ProductResponse và inject ProductMapper vào ProductServiceImpl.
 */
public interface ProductMapper {
}
