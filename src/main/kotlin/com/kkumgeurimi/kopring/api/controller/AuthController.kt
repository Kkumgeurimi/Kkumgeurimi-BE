package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.auth.StudentLoginRequest
import com.kkumgeurimi.kopring.api.dto.auth.StudentSignUpRequest
import com.kkumgeurimi.kopring.api.dto.auth.TokenResponse
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val studentService: StudentService
) {
    
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody request: StudentSignUpRequest,
        response: HttpServletResponse
    ): TokenResponse {
        // 회원가입
        studentService.signUp(request)
        
        // 자동 로그인하여 토큰 생성
        val loginRequest = StudentLoginRequest(
            email = request.email,
            password = request.password
        )
        
        val tokenResponse = authService.login(loginRequest)
        
        // 헤더에 토큰 추가
        response.setHeader("Authorization", "Bearer ${tokenResponse.accessToken}")
        
        return tokenResponse
    }
    
    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: StudentLoginRequest,
        response: HttpServletResponse
    ): TokenResponse {
        val tokenResponse = authService.login(request)
        
        // 헤더에 토큰 추가
        response.setHeader("Authorization", "Bearer ${tokenResponse.accessToken}")
        
        return tokenResponse
    }
    
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String) {
        authService.logout(token)
    }
    
    @Operation(summary = "토큰 갱신")
    @PostMapping("/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") token: String,
        response: HttpServletResponse
    ): TokenResponse {
        val student = authService.getStudentFromToken(token)
        val tokenResponse = authService.refreshToken(student.email)
        
        // 헤더에 새 토큰 추가
        response.setHeader("Authorization", "Bearer ${tokenResponse.accessToken}")
        
        return tokenResponse
    }
}
