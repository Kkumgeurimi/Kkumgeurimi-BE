package com.kkumgeurimi.kopring.api.dto

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)
