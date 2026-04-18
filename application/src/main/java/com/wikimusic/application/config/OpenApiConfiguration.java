package com.wikimusic.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfiguration — Configures the Swagger UI / OpenAPI 3 documentation.
 * Available at: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * Creates the {@link OpenAPI} bean with WikiMusic API metadata.
     *
     * @return the configured OpenAPI specification
     */
    @Bean
    public OpenAPI wikimusicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WikiMusic API")
                        .description("Music knowledge base REST API — Hexagonal Architecture with Spring Boot 3")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("WikiMusic Team")
                                .email("contact@wikimusic.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
