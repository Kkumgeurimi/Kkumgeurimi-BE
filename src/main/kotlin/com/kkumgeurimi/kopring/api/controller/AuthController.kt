package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.*
import com.kkumgeurimi.kopring.domain.student.entity.toResponse
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
    fun signUp(@Valid @RequestBody request: StudentSignUpRequest): StudentResponse {
        return studentService.signUp(request).toResponse()
    }
    
    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: StudentLoginRequest): TokenResponse {
        return authService.login(request)
    }
    
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String) {
        authService.logout(token)
    }
    
    @Operation(summary = "토큰 갱신")
    @PostMapping("/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") token: String
    ): TokenResponse {
        val student = authService.getCurrentStudent(token)
        return authService.refreshToken(student.email)
    }
}
