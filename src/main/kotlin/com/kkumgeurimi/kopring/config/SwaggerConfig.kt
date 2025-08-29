package com.kkumgeurimi.kopring.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.Components
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class SwaggerConfig(
    private val environment: Environment
) {

    @Bean
    fun openAPI(): OpenAPI {
        // 프로덕션 환경에서는 HTTPS URL 사용
        val serverUrl = if (environment.activeProfiles.contains("prod")) {
            "https://api.kkumgeurimi.r-e.kr"
        } else {
            "http://localhost:8080"
        }

        return OpenAPI()
            .servers(
                listOf(
                    Server()
                        .url(serverUrl)
                        .description("API Server")
                )
            )
            .info(
                Info()
                    .title("Kopring API")
                    .version("1.0.0")
                    .description("Kopring REST API 문서")
            )
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요")
                    )
            )
    }
}
