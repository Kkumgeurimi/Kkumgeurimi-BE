package com.kkumgeurimi.kopring.api.dto

import jakarta.validation.constraints.NotNull

data class InterestCategoryRequest(
    @field:NotNull(message = "관심사 카테고리 ID는 필수입니다")
    val interestCategory: Int
)
