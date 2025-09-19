package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class MyProgramWithRegistrationStatusResponse(
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val imageUrl: String?,
    val interestCategoryLabel: String?,
    val registrationStatus: RegistrationStatus?,
    val programRegistrationId: Long,

    // 추가 필드
    val isReviewed: Boolean,
    val reviewScore: String?,
    val reviewMessage: String?,
    val experienceDate: LocalDateTime? // modifiedAt 임시 활용
) {
    companion object {
        fun from(registration: ProgramRegistration): MyProgramWithRegistrationStatusResponse {
            val program = registration.program

            val interestCategoryLabel = program.interestCategory?.let {
                InterestCategory.fromCode(it).label
            }

            return MyProgramWithRegistrationStatusResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                startDate = program.startDate,
                endDate = program.endDate,
                imageUrl = program.imageUrl,
                interestCategoryLabel = interestCategoryLabel,
                registrationStatus = registration.registrationStatus,
                programRegistrationId = registration.programRegistrationId,
                isReviewed = !registration.reviewScore.isNullOrBlank() || !registration.reviewMessage.isNullOrBlank(),
                reviewScore = registration.reviewScore,
                reviewMessage = registration.reviewMessage,
                experienceDate = registration.modifiedAt // BaseTime에서 상속받은 값
            )
        }
    }
}
