package com.learning.backend.menu.mapper;

import com.learning.backend.menu.dto.CategoryResponse;
import com.learning.backend.menu.dto.MenuItemRequest;
import com.learning.backend.menu.dto.MenuItemResponse;
import com.learning.backend.menu.entity.Category;
import com.learning.backend.menu.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Phiên dịch viên 2 chiều giữa Entity (DB) <-> DTO (JSON Mobile).
 * Khi compile (`./mvnw -o compile -q`), MapStruct tự sinh class
 * `MenuItemMapperImpl` thay bạn!
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper {

    // Hàm mẫu 1: Category -> CategoryResponse (tên trường giống nhau 100% nên không
    // cần @Mapping!)
    CategoryResponse toCategoryResponse(Category category);

    // TODO 1: Viết hàm `MenuItemResponse toResponse(MenuItem entity);`
    // Gắn 3 dòng @Mapping lên trên hàm để lấy dữ liệu lồng bên trong `category` và
    // `status`:
    // @Mapping(source = "category.id", target = "categoryId")
    // @Mapping(source = "category.name", target = "categoryName")
    // @Mapping(source = "status.label", target = "statusLabel")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "status.label", target = "statusLabel")

    MenuItemResponse toResponse(MenuItem menuItem);

    @Mapping(target = "status", defaultValue = "AVAILABLE")
    MenuItem toEntity(MenuItemRequest request);

    // TODO 2: Viết hàm `MenuItem toEntity(MenuItemRequest request);`
    // (Các trường code, name, price, imageUrl, description, status trùng tên nhau
    // nên không cần @Mapping;
    // riêng `category` sẽ được Service tìm từ categoryId và gán vào sau.)
}
