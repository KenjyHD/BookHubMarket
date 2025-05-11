package com.kenjy.bookapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BASIC_AUTH_SECURITY_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Basic authentication")
                        )
                )
                .info(new Info()
                        .title(applicationName)
                        .version("1.0")
                        .description("Book API Documentation")
                )
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH_SECURITY_SCHEME));
    }

    public static final String BASIC_AUTH_SECURITY_SCHEME = "basicAuth";
}
