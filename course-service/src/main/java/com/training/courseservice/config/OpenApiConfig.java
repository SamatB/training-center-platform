package com.training.courseservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI courseServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Course Service API")
                        .description("API для работы с курсами Training Center Platform")
                        .version("v1"));
    }
}
