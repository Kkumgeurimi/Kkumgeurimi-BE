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
        val servers = mutableListOf<Server>()
        
        // 로컬 서버 추가 (개발 환경)
        servers.add(
            Server()
                .url("http://localhost:8080")
                .description("Local Development Server")
        )
        
        // 프로덕션 서버 추가
        servers.add(
            Server()
                .url("https://api.kkumgeurimi.r-e.kr")
                .description("Production API Server")
        )
        
        // 개발 환경에서는 로컬 서버를 기본값으로 설정
        val defaultServer = if (environment.activeProfiles.contains("prod")) {
            "https://api.kkumgeurimi.r-e.kr"
        } else {
            "http://localhost:8080"
        }
        
        return OpenAPI()
            .servers(servers)
            .info(
                Info()
                    .title("Kopring API")
                    .version("1.0.0")
                    .description("Kopring REST API 문서\n\n" +
                        "서버를 선택하여 API를 테스트할 수 있습니다:\n" +
                        "- **Local Development Server**: 로컬 개발 환경\n" +
                        "- **Production API Server**: 프로덕션 환경")
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
