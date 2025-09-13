package com.kkumgeurimi.kopring.api.dto.auth

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class InterestCategoryRequest(
    @field:NotNull(message = "관심사 카테고리 ID는 필수입니다")
    @field:Min(0)
    @field:Max(31)
    val interestCategory: Int
)
