package com.learning.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình tài liệu Swagger / OpenAPI 3.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini Store Backend Lab API")
                        .version("1.0.0")
                        .description("REST API học tập & thực hành Backend Java với Spring Boot 3 & H2 Database")
                        .contact(new Contact().name("Backend Learner").email("learner@example.com"))
                        .license(new License().name("Apache 2.0")));
    }
}

