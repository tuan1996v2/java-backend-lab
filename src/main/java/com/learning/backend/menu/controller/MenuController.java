package com.learning.backend.menu.controller;

import com.learning.backend.common.dto.ApiResponse;
import com.learning.backend.menu.dto.CategoryResponse;
import com.learning.backend.menu.dto.MenuItemResponse;
import com.learning.backend.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cửa ngõ nhận HTTP Request từ App Mobile cho phân hệ Thực đơn (/api/menu).
 * Nhiệm vụ của Controller rất nhẹ: nhận request -> gọi MenuService -> bọc vào
 * ApiResponse.ok(...) trả JSON.
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu (App Khách)", description = "API xem danh mục và món ăn")
public class MenuController {

    private final MenuService menuService;

    /** API mẫu: GET http://localhost:8080/api/menu/categories */
    @Operation(summary = "Lấy danh sách danh mục món ăn")
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.ok(menuService.getCategories());
    }

    /**
     * BÀI NHỎ 2 (Nấc 1 — Mở API GET List món ăn thuần lên Swagger UI):
     * - Ngữ cảnh App Mobile: Màn hình Thực đơn gọi `GET /api/menu` để lấy toàn bộ
     * danh sách món ăn.
     * - Yêu cầu cụ thể:
     * 1. Gắn `@GetMapping` (không cần truyền đường dẫn con, vì mặc định ăn theo
     * `/api/menu`).
     * 2. Trong thân hàm, gọi `menuService.getAllMenuItems()`, bọc vào
     * `ApiResponse.ok(...)` rồi `return`.
     * - Ví dụ JSON trả về cho Mobile:
     * `{ "success": true, "message": "Thành công", "data": [ { "id": 1, "code":
     * "MON-001", "name": "Gỏi Ngó Sen...", ... } ], "timestamp": "..." }`
     * - Công cụ / Cú pháp Java (giống hệt hàm `getCategories()` ngay bên trên):
     * `@GetMapping` + `return ApiResponse.ok(menuService.getAllMenuItems());`
     */
    @Operation(summary = "Lấy danh sách món ăn")
    @GetMapping()
    public ApiResponse<List<MenuItemResponse>> getAllMenuItems() {
        return ApiResponse.ok(menuService.getAllMenuItems());
    }

    /**
     * BÀI TẬP NẤC 2 (Cháo — Lấy chi tiết 1 món theo ID trên URL):
     * - Ngữ cảnh App Mobile: Khách bấm vào 1 món trên FlatList để mở màn hình Chi
     * tiết món ăn (`GET /api/menu/3`).
     * - Yêu cầu cụ thể:
     * 1. Gắn `@Operation(summary = "Xem chi tiết 1 món ăn theo ID")`
     * 2. Gắn `@GetMapping("/{id}")`
     * 3. Khai báo tham số `@PathVariable Long id` để hứng con số trên URL.
     * 4. Gọi `menuService.getMenuItemDetail(id)` và bọc vào `ApiResponse.ok(...)`
     * trả về.
     * - Ví dụ luồng chạy:
     * + `GET /api/menu/3` -> `{ "success": true, "data": { "id": 3, "name": "Phở Bò
     * Tái Nạm...", ... } }`
     * + `GET /api/menu/999` -> Service tự ném lỗi 404.
     * - Công cụ: `@GetMapping("/{id}")`, `@PathVariable Long id`,
     * `ApiResponse.ok(...)`.
     */
    // TODO (Nấc 2): Tự viết hàm `getMenuItemDetail` tại đây
    @Operation(summary = "Lấy chi tiết 1 món theo ID")
    @GetMapping("/{id}")
    public ApiResponse<MenuItemResponse> getMenuItemDetail(@PathVariable Long id) {
        return ApiResponse.ok(menuService.getMenuItemDetail(id));
    }

    /**
     * BÀI TẬP NẤC 2.2 (Phần 3/3 — Controller nhận @RequestParam):
     * - Ngữ cảnh App Mobile: App gọi `GET /api/menu/by-category?categoryId=2`.
     * - Yêu cầu cụ thể:
     * 1. Gắn `@Operation(summary = "Lọc món ăn theo danh mục")`
     * 2. Gắn `@GetMapping("/by-category")`
     * 3. Hứng tham số `@RequestParam Long categoryId`
     * 4. Trả về `ApiResponse.ok(menuService.getMenuItemsByCategory(categoryId))`
     * - Ví dụ URL: `GET /api/menu/by-category?categoryId=2` -> JSON `{ "success":
     * true, "data": [ ... 3 món chính ... ] }`
     * - Công cụ: `@GetMapping("/by-category")`, `@RequestParam Long categoryId`,
     * `ApiResponse.ok(...)`.
     */
    // TODO: Viết API GET /api/menu/by-category tại đây

    @Operation(summary = "Lọc món ăn theo danh mục")
    @GetMapping("/by-category")
    public ApiResponse<List<MenuItemResponse>> getMenuByCategory(@RequestParam Long categoryId) {
        return ApiResponse.ok(menuService.getMenuItemsByCategory(categoryId));
    }

}
