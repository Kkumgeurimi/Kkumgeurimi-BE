package com.kkumgeurimi.kopring.api.dto.review

import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration

data class CompletedProgramDto(
    val programRegistrationId: Long,
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val imageUrl: String?,
    val endDate: String?,
    val hasReview: Boolean,
    val reviewScore: String?,
    val reviewMessage: String?
) {
    companion object {
        fun from(registration: ProgramRegistration): CompletedProgramDto {
            return CompletedProgramDto(
                programRegistrationId = registration.programRegistrationId,
                programId = registration.program.programId,
                programTitle = registration.program.programTitle,
                provider = registration.program.provider,
                imageUrl = registration.program.imageUrl,
                endDate = registration.program.endDate?.toString(),
                hasReview = registration.reviewScore != null,
                reviewScore = registration.reviewScore,
                reviewMessage = registration.reviewMessage
            )
        }
    }
}
