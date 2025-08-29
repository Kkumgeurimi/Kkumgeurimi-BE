package com.kkumgeurimi.kopring.api.exception

class CustomException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
