package com.learning.backend.common.config;

import com.learning.backend.menu.entity.Category;
import com.learning.backend.menu.entity.MenuItem;
import com.learning.backend.menu.entity.MenuItemStatus;
import com.learning.backend.menu.repository.CategoryRepository;
import com.learning.backend.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tự động nạp dữ liệu Nhà hàng mẫu khi khởi động app (nếu bảng đang trống).
 * - CommandLineRunner: Spring Boot tự gọi hàm run(...) ngay sau khi khởi động xong.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0 || menuItemRepository.count() > 0) {
            return;
        }

        Category khaiVi = categoryRepository.save(Category.builder()
                .name("Khai vị")
                .sortOrder(1)
                .build());
        Category monChinh = categoryRepository.save(Category.builder()
                .name("Món chính")
                .sortOrder(2)
                .build());
        Category lauNuong = categoryRepository.save(Category.builder()
                .name("Lẩu & Nướng")
                .sortOrder(3)
                .build());
        Category doUong = categoryRepository.save(Category.builder()
                .name("Đồ uống")
                .sortOrder(4)
                .build());

        List<MenuItem> items = List.of(
                MenuItem.builder()
                        .category(khaiVi)
                        .code("MON-001")
                        .name("Gỏi Ngó Sen Tôm Thịt")
                        .price(new BigDecimal("95000"))
                        .description("Ngó sen giòn kết hợp tôm sú và thịt ba chỉ")
                        .build(),
                MenuItem.builder()
                        .category(khaiVi)
                        .code("MON-002")
                        .name("Chả Giò Hải Sản")
                        .price(new BigDecimal("85000"))
                        .description("Vỏ giòn rụm, nhân tôm cua thịt băm")
                        .build(),
                MenuItem.builder()
                        .category(monChinh)
                        .code("MON-003")
                        .name("Phở Bò Tái Nạm Đặc Biệt")
                        .price(new BigDecimal("75000"))
                        .description("Nước dùng ninh xương 12 tiếng, thịt bò tươi")
                        .build(),
                MenuItem.builder()
                        .category(monChinh)
                        .code("MON-004")
                        .name("Cơm Chiên Dương Châu")
                        .price(new BigDecimal("89000"))
                        .description("Hạt cơm tơi, tôm, lạp xưởng, trứng gà")
                        .build(),
                MenuItem.builder()
                        .category(monChinh)
                        .code("MON-005")
                        .name("Bò Lúc Lắc Khoai Tây")
                        .price(new BigDecimal("165000"))
                        .description("Thăn bò Úc áp chảo sốt tiêu đen")
                        .build(),
                MenuItem.builder()
                        .category(lauNuong)
                        .code("MON-006")
                        .name("Lẩu Thái Tomyum Hải Sản")
                        .price(new BigDecimal("329000"))
                        .description("Nồi lớn cho 4 người: tôm, mực, nghêu, nấm")
                        .build(),
                MenuItem.builder()
                        .category(lauNuong)
                        .code("MON-007")
                        .name("Ba Chỉ Bò Mỹ Nướng Sốt Mè")
                        .price(new BigDecimal("189000"))
                        .description("Thái lát mỏng, ăn kèm rau xà lách và kim chi")
                        .build(),
                MenuItem.builder()
                        .category(lauNuong)
                        .code("MON-008")
                        .name("Cua Cà Mau Hấp Bia")
                        .price(new BigDecimal("450000"))
                        .status(MenuItemStatus.SOLD_OUT)
                        .description("Hết hàng trong ngày — dùng để test lọc trạng thái")
                        .build(),
                MenuItem.builder()
                        .category(doUong)
                        .code("MON-009")
                        .name("Trà Đào Cam Sả")
                        .price(new BigDecimal("45000"))
                        .description("Ly lớn mát lạnh")
                        .build(),
                MenuItem.builder()
                        .category(doUong)
                        .code("MON-010")
                        .name("Nước Ép Dưa Hấu Tươi")
                        .price(new BigDecimal("40000"))
                        .description("Ép nguyên chất không đường")
                        .build());

        menuItemRepository.saveAll(items);
        log.info("Đã nạp {} danh mục và {} món ăn mẫu vào restaurantdb.", 4, items.size());
    }
}
