package com.kkumgeurimi.kopring.config

import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {
    
    @Value("\${spring.security.jwt.secret}")
    private lateinit var secretKey: String
    
    @Value("\${spring.security.jwt.expiration}")
    private lateinit var expiration: String
    
    private val key: Key by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
    
    fun generateToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration.toLong())
        
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun generateAccessToken(email: String): String {
        return generateToken(email)
    }
    
    fun generateRefreshToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + 2 * 24 * 60 * 60 * 1000L) // 2일
        
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun getEmailFromToken(token: String): String {
        return getClaims(token).subject
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            !isTokenExpired(token)
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            false
        }
    }
    
    fun isTokenValid(token: String): Boolean {
        return validateToken(token)
    }
    
    fun getExpirationFromToken(token: String): Date {
        return getClaims(token).expiration
    }
    
    fun isTokenExpired(token: String): Boolean {
        return try {
            getClaims(token).expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            true
        }
    }
    
    fun getRemainingTimeInSeconds(token: String): Long {
        val expirationDate = getClaims(token).expiration
        val currentTime = Date()
        return (expirationDate.time - currentTime.time) / 1000
    }
    
    private fun getClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }
    }
}
