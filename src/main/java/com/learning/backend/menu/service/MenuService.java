package com.learning.backend.menu.service;

import com.learning.backend.menu.dto.CategoryResponse;
import com.learning.backend.menu.dto.MenuItemResponse;

import java.util.List;

/**
 * Hợp đồng nghiệp vụ của phân hệ Thực đơn (Bắt đầu từ đơn giản nhất: lấy List thuần).
 */
public interface MenuService {

    List<CategoryResponse> getCategories();

    List<MenuItemResponse> getAllMenuItems();

    MenuItemResponse getMenuItemDetail(Long id);
}
