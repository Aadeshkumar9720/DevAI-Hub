package com.devaihub.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("DevAI Hub REST API")
                                .version("v1.0")
                                .description("""
            DevAI Hub Backend API

            Features:
            • JWT Authentication
            • Project Management
            • Task Management
            • Team Collaboration
            • Comments
            • Attachments
            • Dashboard
            • Activity Timeline
            • Real-time Notifications
            • Email Notifications
            """)
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
