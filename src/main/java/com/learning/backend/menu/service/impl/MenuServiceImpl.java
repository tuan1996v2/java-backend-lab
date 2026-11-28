package com.learning.backend.menu.service.impl;

import com.learning.backend.common.exception.ResourceNotFoundException;
import com.learning.backend.menu.dto.CategoryResponse;
import com.learning.backend.menu.dto.MenuItemResponse;
import com.learning.backend.menu.entity.MenuItem;
import com.learning.backend.menu.mapper.MenuItemMapper;
import com.learning.backend.menu.repository.CategoryRepository;
import com.learning.backend.menu.repository.MenuItemRepository;
import com.learning.backend.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private static final Long DEFAULT_BRANCH_ID = 1L;

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    /** Hàm mẫu: Lấy danh sách danh mục */
    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository
                .findByBranchIdAndActiveTrueAndDeletedFalseOrderBySortOrderAsc(DEFAULT_BRANCH_ID)
                .stream()
                .map(menuItemMapper::toCategoryResponse)
                .toList();
    }

    /**
     * BÀI NHỎ 1 (Nấc 1 — Lấy List món ăn thuần):
     * - Ngữ cảnh App Mobile: Khách vừa quét QR mở màn hình "Thực đơn" lần đầu, App
     * cần tải danh sách món ăn để hiển thị lên FlatList.
     * - Yêu cầu cụ thể:
     * 1. Lấy toàn bộ bản ghi `MenuItem` từ DB thông qua
     * `menuItemRepository.findAll()`.
     * 2. Chuyển đổi từng `MenuItem` (Entity DB thô) sang `MenuItemResponse` (JSON
     * phẳng cho Mobile) bằng `menuItemMapper::toResponse`.
     * 3. Trả về `List<MenuItemResponse>`.
     * - Ví dụ luồng dữ liệu:
     * + DB đang có 10 `MenuItem` (`MON-001` Phở Bò, `MON-006` Lẩu Thái...)
     * + Đầu ra: List 10 JSON `{ id, categoryId, categoryName: "Món chính", code:
     * "MON-001", name: "Phở Bò...", statusLabel: "Còn món", ... }`
     * - Công cụ / Cú pháp Java (giống hệt `getCategories()` ở trên):
     * `menuItemRepository.findAll().stream().map(menuItemMapper::toResponse).toList()`
     */
    @Override
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(menuItemMapper::toResponse)
                .toList();
    }

    /** Hàm xem chi tiết 1 món (code bạn đã tự viết chuẩn 100%) */
    @Override
    public MenuItemResponse getMenuItemDetail(Long id) {
        MenuItem item = menuItemRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn với id: " + id));
        return menuItemMapper.toResponse(item);
    }
}
