package com.learning.backend.config;

import com.learning.backend.entity.Product;
import com.learning.backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tự động nạp sẵn dữ liệu mẫu vào H2 Database khi ứng dụng khởi chạy.
 * Giúp người học có thể gọi API GET hoặc xem H2 Console ngay lập tức mà không phải tự tạo từ đầu.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                List<Product> initialProducts = List.of(
                        new Product("TH-MILK-001", "Sữa tươi tiệt trùng TH True MILK ít đường 180ml",
                                new BigDecimal("38000"), 120, "Sữa tươi", "Lốc 4 hộp x 180ml"),
                        new Product("TH-MILK-002", "Sữa tươi tiệt trùng TH True MILK nguyên chất 1L",
                                new BigDecimal("42000"), 50, "Sữa tươi", "Hộp 1 lít"),
                        new Product("TH-YOGURT-001", "Sữa chua ăn TH True YOGURT tự nhiên",
                                new BigDecimal("28000"), 80, "Sữa chua", "Lốc 4 hộp x 100g"),
                        new Product("TH-CHEESE-001", "Phô mai que TH True CHEESE Mozzarella",
                                new BigDecimal("65000"), 35, "Bơ - Phô mai", "Gói 100g"),
                        new Product("TH-ICE-001", "Kem que TH True ICE CREAM sô-cô-la",
                                new BigDecimal("18000"), 200, "Kem", "Cây 70g")
                );

                productRepository.saveAll(initialProducts);
                System.out.println("✅ Đã khởi tạo thành công " + initialProducts.size() + " sản phẩm mẫu vào Database.");
            }
        };
    }
}

