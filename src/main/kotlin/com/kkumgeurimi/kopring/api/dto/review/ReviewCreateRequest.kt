package com.kkumgeurimi.kopring.api.dto.review

import jakarta.validation.constraints.NotBlank

data class ReviewCreateRequest(
    @field:NotBlank(message = "리뷰 점수는 필수입니다.")
    val reviewScore: String,
    val reviewMessage: String?
)
