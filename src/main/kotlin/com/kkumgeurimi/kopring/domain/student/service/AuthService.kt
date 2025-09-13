package com.kkumgeurimi.kopring.domain.student.service

import com.kkumgeurimi.kopring.api.dto.auth.TokenResponse
import com.kkumgeurimi.kopring.api.dto.auth.StudentLoginRequest
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.config.JwtUtil
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AuthService(
    private val studentService: StudentService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val redisTemplate: RedisTemplate<String, String>
) {
    
    fun login(request: StudentLoginRequest): TokenResponse {
        // 사용자 조회
        val student = studentService.findByEmail(request.email)
        
        // 비밀번호 확인
        if (!passwordEncoder.matches(request.password, student.password)) {
            throw CustomException(ErrorCode.INVALID_CREDENTIALS)
        }
        
        // JWT 토큰 생성
        val accessToken = jwtUtil.generateAccessToken(student.email)
        val refreshToken = jwtUtil.generateRefreshToken(student.email)
        
        // Redis에 Refresh Token 저장 (2일)
        redisTemplate.opsForValue().set(
            "refresh_token:${student.email}",
            refreshToken,
            2,
            TimeUnit.DAYS
        )
        
        return TokenResponse(
            accessToken = accessToken,
            tokenType = "Bearer"
        )
    }
    
    fun logout(token: String) {
        val cleanToken = token.replace("Bearer ", "")
        
        // 토큰 유효성 검사
        if (!jwtUtil.isTokenValid(cleanToken)) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }
        
        val email = jwtUtil.getEmailFromToken(cleanToken)
        
        // 블랙리스트에 추가 (토큰 만료시간까지)
        val expiration = jwtUtil.getExpirationFromToken(cleanToken)
        val now = System.currentTimeMillis()
        val ttl = (expiration.time - now) / 1000
        
        if (ttl > 0) {
            redisTemplate.opsForValue().set(
                "blacklist:$cleanToken",
                "true",
                ttl,
                TimeUnit.SECONDS
            )
        }
        
        // Refresh Token 삭제
        redisTemplate.delete("refresh_token:$email")
    }
    
    fun refreshToken(email: String): TokenResponse {
        // Redis에서 Refresh Token 확인
        val storedRefreshToken = redisTemplate.opsForValue().get("refresh_token:$email")
            ?: throw CustomException(ErrorCode.INVALID_REFRESH_TOKEN)
        
        // 새로운 Access Token 생성
        val newAccessToken = jwtUtil.generateAccessToken(email)
        
        return TokenResponse(
            accessToken = newAccessToken,
            tokenType = "Bearer"
        )
    }
    
    fun getCurrentStudent(): Student {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        return studentService.findByEmail(email)
    }
    
    fun getStudentFromToken(token: String): Student {
        val cleanToken = token.replace("Bearer ", "")
        
        // 토큰 유효성 검사
        if (!jwtUtil.isTokenValid(cleanToken)) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }
        
        // 블랙리스트 확인
        if (redisTemplate.hasKey("blacklist:$cleanToken")) {
            throw CustomException(ErrorCode.BLACKLISTED_TOKEN)
        }
        
        val email = jwtUtil.getEmailFromToken(cleanToken)
        return studentService.findByEmail(email)
    }

    fun getCurrentStudentOrNull(): Student? {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        return studentService.findByEmailOrNull(email)
    }
}
