package com.learning.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Điểm khởi chạy chính của hệ thống Restaurant Backend (F&B Learning Lab).
 * - @EnableJpaAuditing: Tự động điền createdAt và updatedAt trong BaseEntity.
 */
@SpringBootApplication
@EnableJpaAuditing
public class JavaBackendLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaBackendLabApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🍽️  Restaurant Backend API đã khởi động thành công!");
        System.out.println("📄 Swagger UI:  http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️  H2 Database: http://localhost:8080/h2-console");
        System.out.println("   (JDBC URL: jdbc:h2:mem:restaurantdb | User: sa | Password để trống)");
        System.out.println("=================================================");
    }
}

