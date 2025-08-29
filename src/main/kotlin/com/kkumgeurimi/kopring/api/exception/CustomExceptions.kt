package com.kkumgeurimi.kopring.api.exception

class CustomException(
    val errorCode: ErrorCode,
    val detailMessage: String? = null
) : RuntimeException(detailMessage ?: errorCode.message)
