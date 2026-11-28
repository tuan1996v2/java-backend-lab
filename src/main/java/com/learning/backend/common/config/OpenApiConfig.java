package com.learning.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình tài liệu Swagger / OpenAPI 3 tại /swagger-ui.html.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Backend API (F&B Learning Lab)")
                        .version("1.0.0")
                        .description("REST API cho hệ thống Nhà hàng F&B (App Khách -> Bếp -> Thu ngân -> Kho & Chuỗi chi nhánh)")
                        .contact(new Contact().name("Full-Stack Learner").email("learner@example.com")));
    }
}
