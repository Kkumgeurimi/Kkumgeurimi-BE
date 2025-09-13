package com.kkumgeurimi.kopring.api.dto.auth

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)
