package com.learning.backend.menu.dto;

import com.learning.backend.menu.entity.MenuItemStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * JSON Quản lý gửi lên khi Tạo mới / Sửa món ăn (POST / PUT).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {

    @NotNull(message = "Danh mục món là bắt buộc")
    private Long categoryId;

    @NotBlank(message = "Mã món (code) không được để trống")
    private String code;

    @NotBlank(message = "Tên món không được để trống")
    private String name;

    @NotNull(message = "Giá bán là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá món phải lớn hơn 0")
    private BigDecimal price;

    private String imageUrl;
    private String description;
    private MenuItemStatus status;
}
