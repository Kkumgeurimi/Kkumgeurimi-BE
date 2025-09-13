package com.kkumgeurimi.kopring.api.dto.review

import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import java.time.LocalDateTime

data class MyReviewResponse(
    val programRegistrationId: Long,
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val imageUrl: String?,
    val reviewScore: String?,
    val reviewMessage: String?,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(registration: ProgramRegistration): MyReviewResponse {
            return MyReviewResponse(
                programRegistrationId = registration.programRegistrationId,
                programId = registration.program.programId,
                programTitle = registration.program.programTitle,
                provider = registration.program.provider,
                imageUrl = registration.program.imageUrl,
                reviewScore = registration.reviewScore,
                reviewMessage = registration.reviewMessage,
                createdAt = registration.createdAt
            )
        }
    }
}
