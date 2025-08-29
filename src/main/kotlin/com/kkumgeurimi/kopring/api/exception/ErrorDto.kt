package com.kkumgeurimi.kopring.api.exception

import java.time.LocalDateTime

data class ErrorDto(
    val timestamp: String = LocalDateTime.now().toString(),
    val status: Int,
    val errorCode: String,
    val message: String,
    val path: String
)
