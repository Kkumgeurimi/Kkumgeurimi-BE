package com.kkumgeurimi.kopring.config

import com.kkumgeurimi.kopring.domain.student.service.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val authService: AuthService
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
            val student = authService.getStudentFromToken(token)
            
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
