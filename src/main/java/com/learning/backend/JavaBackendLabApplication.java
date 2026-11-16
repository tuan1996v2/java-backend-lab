package com.learning.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Điểm khởi chạy chính của ứng dụng Spring Boot.
 * 
 * @SpringBootApplication bao gồm:
 * - @Configuration: Đánh dấu class cấu hình.
 * - @EnableAutoConfiguration: Tự động cấu hình các thư viện có trong classpath.
 * - @ComponentScan: Tự động quét và phát hiện các @Component, @Service, @Repository, @Controller.
 */
@SpringBootApplication
public class JavaBackendLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaBackendLabApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 Backend Lab đã khởi động thành công!");
        System.out.println("📄 Swagger UI:  http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️ H2 Database: http://localhost:8080/h2-console");
        System.out.println("   (JDBC URL: jdbc:h2:mem:storedb | User: sa | Password để trống)");
        System.out.println("=================================================");
    }
}

