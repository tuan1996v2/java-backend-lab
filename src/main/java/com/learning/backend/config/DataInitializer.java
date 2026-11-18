package com.learning.backend.config;

import com.learning.backend.entity.Product;
import com.learning.backend.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
public class DataInitializer {

        @Bean
        public CommandLineRunner initData(ProductRepository productRepository) {
                return args -> {
                        if (productRepository.count() == 0) {
                                List<Product> initialProducts = List.of(
                                                // --- 1. SỮA TƯƠI (8 items) ---
                                                buildProduct("TH-MILK-001",
                                                                "Sữa tươi tiệt trùng TH True MILK ít đường 180ml",
                                                                "38000", 120, "Sữa tươi", "Lốc 4 hộp x 180ml", 10),
                                                buildProduct("TH-MILK-002",
                                                                "Sữa tươi tiệt trùng TH True MILK nguyên chất 1L",
                                                                "42000", 50, "Sữa tươi", "Hộp 1 lít", 0),
                                                buildProduct("TH-MILK-003",
                                                                "Sữa tươi tiệt trùng TH True MILK có đường 180ml",
                                                                "38000", 150, "Sữa tươi", "Lốc 4 hộp x 180ml", 5),
                                                buildProduct("TH-MILK-004",
                                                                "Sữa tươi tiệt trùng TH True MILK không đường 180ml",
                                                                "38000", 95, "Sữa tươi", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-MILK-005",
                                                                "Sữa tươi tiệt trùng TH True MILK hương dâu 180ml",
                                                                "39000", 80, "Sữa tươi", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-MILK-006",
                                                                "Sữa tươi tiệt trùng TH True MILK sô-cô-la 180ml",
                                                                "39000", 85, "Sữa tươi", "Lốc 4 hộp x 180ml", 5),
                                                buildProduct("TH-MILK-007",
                                                                "Sữa tươi thanh trùng TH True MILK nguyên chất 900ml",
                                                                "48000", 30, "Sữa tươi",
                                                                "Chai nhựa 900ml bảo quản lạnh", 0),
                                                buildProduct("TH-MILK-008",
                                                                "Sữa tươi hữu cơ TH True MILK Organic 500ml", "35000",
                                                                40, "Sữa tươi", "Hộp giấy 500ml đạt chuẩn EU", 15),

                                                // --- 2. SỮA HẠT (6 items) ---
                                                buildProduct("TH-NUT-001", "Sữa hạt óc chó TH True NUT 180ml", "49000",
                                                                90, "Sữa hạt",
                                                                "Lốc 4 hộp x 180ml vị ngọt tự nhiên từ quả chà là", 10),
                                                buildProduct("TH-NUT-002", "Sữa hạt hạnh nhân TH True NUT 180ml",
                                                                "49000", 75, "Sữa hạt", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-NUT-003", "Sữa hạt mắc ca TH True NUT 180ml", "52000",
                                                                60, "Sữa hạt", "Lốc 4 hộp x 180ml", 5),
                                                buildProduct("TH-NUT-004", "Sữa gạo lứt đỏ TH True NUT 180ml", "44000",
                                                                110, "Sữa hạt", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-NUT-005", "Sữa hạt gấc tự nhiên TH True NUT 180ml",
                                                                "46000", 55, "Sữa hạt", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-NUT-006", "Sữa hạt nghệ TH True NUT 180ml", "46000",
                                                                45, "Sữa hạt", "Lốc 4 hộp x 180ml", 10),

                                                // --- 3. SỮA CHUA ĂN & UỐNG (8 items) ---
                                                buildProduct("TH-YOGURT-001", "Sữa chua ăn TH True YOGURT tự nhiên",
                                                                "28000", 80, "Sữa chua", "Lốc 4 hộp x 100g", 0),
                                                buildProduct("TH-YOGURT-002", "Sữa chua ăn TH True YOGURT có đường",
                                                                "28000", 140, "Sữa chua", "Lốc 4 hộp x 100g", 5),
                                                buildProduct("TH-YOGURT-003", "Sữa chua ăn TH True YOGURT nha đam",
                                                                "32000", 100, "Sữa chua", "Lốc 4 hộp x 100g", 0),
                                                buildProduct("TH-YOGURT-004",
                                                                "Sữa chua ăn TH True YOGURT trân châu đường đen",
                                                                "34000", 70, "Sữa chua", "Lốc 4 hộp x 100g", 10),
                                                buildProduct("TH-YOGURT-005", "Sữa chua ăn TH True YOGURT việt quất",
                                                                "32000", 65, "Sữa chua", "Lốc 4 hộp x 100g", 0),
                                                buildProduct("TH-YOGURT-006",
                                                                "Sữa chua uống tiệt trùng TH True YOGURT hương cam 180ml",
                                                                "31000", 130, "Sữa chua", "Lốc 4 hộp x 180ml", 0),
                                                buildProduct("TH-YOGURT-007",
                                                                "Sữa chua uống tiệt trùng TH True YOGURT dâu tự nhiên 180ml",
                                                                "31000", 125, "Sữa chua", "Lốc 4 hộp x 180ml", 5),
                                                buildProduct("TH-YOGURT-008",
                                                                "Sữa chua uống men sống TH True YOGURT Probiotics 85ml",
                                                                "26000", 90, "Sữa chua", "Lốc 5 chai x 85ml", 0),

                                                // --- 4. BƠ - PHÔ MAI (4 items) ---
                                                buildProduct("TH-CHEESE-001", "Phô mai que TH True CHEESE Mozzarella",
                                                                "65000", 35, "Bơ - Phô mai", "Gói 100g (4 que)", 0),
                                                buildProduct("TH-CHEESE-002", "Phô mai lát TH True CHEESE Cheddar 200g",
                                                                "78000", 25, "Bơ - Phô mai", "Gói 10 lát x 20g", 10),
                                                buildProduct("TH-BUTTER-001", "Bơ lạt tự nhiên TH True BUTTER 200g",
                                                                "68000", 40, "Bơ - Phô mai",
                                                                "Hộp 200g làm từ sữa tươi nguyên chất", 0),
                                                buildProduct("TH-BUTTER-002", "Bơ mặn tự nhiên TH True BUTTER 200g",
                                                                "68000", 30, "Bơ - Phô mai", "Hộp 200g", 0),

                                                // --- 5. KEM (6 items) ---
                                                buildProduct("TH-ICE-001", "Kem que TH True ICE CREAM sô-cô-la",
                                                                "18000", 200, "Kem", "Cây 70g", 0),
                                                buildProduct("TH-ICE-002", "Kem que TH True ICE CREAM vani tự nhiên",
                                                                "18000", 180, "Kem", "Cây 70g", 0),
                                                buildProduct("TH-ICE-003", "Kem que TH True ICE CREAM trà xanh Matcha",
                                                                "20000", 150, "Kem", "Cây 70g", 5),
                                                buildProduct("TH-ICE-004",
                                                                "Kem ốc quế TH True ICE CREAM sô-cô-la 110ml", "22000",
                                                                110, "Kem", "Cây 110ml", 0),
                                                buildProduct("TH-ICE-005", "Kem hộp TH True ICE CREAM sầu riêng 450ml",
                                                                "85000", 25, "Kem", "Hộp 450ml", 15),
                                                buildProduct("TH-ICE-006", "Kem hộp TH True ICE CREAM dâu tây 450ml",
                                                                "82000", 30, "Kem", "Hộp 450ml", 10),

                                                // --- 6. ĐỒ UỐNG GIẢI KHÁT & NƯỚC TINH KHIẾT (8 items) ---
                                                buildProduct("TH-TEA-001",
                                                                "Trà xanh vị chanh tự nhiên TH True TEA 350ml", "10000",
                                                                300, "Đồ uống giải khát", "Chai 350ml", 0),
                                                buildProduct("TH-TEA-002", "Trà ô long tự nhiên TH True TEA 350ml",
                                                                "10000", 280, "Đồ uống giải khát", "Chai 350ml", 0),
                                                buildProduct("TH-TEA-003", "Trà đào tự nhiên TH True TEA 350ml",
                                                                "11000", 250, "Đồ uống giải khát", "Chai 350ml", 5),
                                                buildProduct("TH-JUICE-001", "Nước ép táo tự nhiên TH True JUICE 350ml",
                                                                "16000", 160, "Đồ uống giải khát",
                                                                "Chai 350ml không thêm đường", 0),
                                                buildProduct("TH-JUICE-002", "Nước ép cam tự nhiên TH True JUICE 350ml",
                                                                "16000", 170, "Đồ uống giải khát", "Chai 350ml", 0),
                                                buildProduct("TH-JUICE-003",
                                                                "Nước uống sữa trái cây TH True JUICE milk cam 300ml",
                                                                "12000", 220, "Đồ uống giải khát", "Chai 300ml", 0),
                                                buildProduct("TH-WATER-001", "Nước tinh khiết TH True WATER 500ml",
                                                                "6000", 500, "Nước tinh khiết", "Chai 500ml", 0),
                                                buildProduct("TH-WATER-002", "Nước tinh khiết TH True WATER 1.5L",
                                                                "12000", 240, "Nước tinh khiết", "Chai 1.5 lít", 0));

                                productRepository.saveAll(initialProducts);
                                log.info("✅ Đã khởi tạo thành công {} sản phẩm mẫu vào Database.",
                                                initialProducts.size());
                        }
                };
        }

        private Product buildProduct(String sku, String name, String price, int stock, String category,
                        String description, int discount) {
                return Product.builder()
                                .sku(sku)
                                .name(name)
                                .price(new BigDecimal(price))
                                .stockQuantity(stock)
                                .category(category)
                                .description(description)
                                .discountPercent(discount)
                                .build();
        }
}