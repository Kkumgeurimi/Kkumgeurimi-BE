package com.kkumgeurimi.kopring.config

import com.kkumgeurimi.kopring.domain.student.repository.StudentRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val studentRepository: StudentRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        
        try {
            val token = authHeader.substring(7) // "Bearer " 제거
            
            // JWT 토큰 유효성 검사
            if (!jwtUtil.isTokenValid(token)) {
                filterChain.doFilter(request, response)
                return
            }
            
            // 토큰에서 이메일 추출
            val email = jwtUtil.getEmailFromToken(token)
            
            // 사용자 정보 조회
            val student = studentRepository.findByEmail(email)
                ?: throw Exception("사용자를 찾을 수 없습니다")
            
            // Spring Security 컨텍스트에 인증 정보 설정
            val authentication = UsernamePasswordAuthenticationToken(
                student.email, // principal
                null, // credentials
                listOf() // authorities
            )
            
            SecurityContextHolder.getContext().authentication = authentication
            
        } catch (e: Exception) {
            // 토큰 검증 실패 시 로그만 남기고 계속 진행
            logger.warn("JWT 토큰 검증 실패: ${e.message}")
        }
        
        filterChain.doFilter(request, response)
    }
}
