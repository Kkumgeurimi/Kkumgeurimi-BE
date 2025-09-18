package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.program.entity.Program
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "프로그램 상세 응답")
data class ProgramDetailResponse(
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val targetAudience: String?,
    val programType: Int?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val relatedMajor: String?,
    val price: String?,
    val imageUrl: String?,
    val venueRegion: String?,
    val likeCount: Long = 0,
    val registrationCount: Long = 0,
    val likedByMe: Boolean = false,
    val registeredByMe: Boolean = false,

    // 프로그램 상세
    val eligibleRegion: String?,
    val interestCategoryLabel: String?,
    val operateCycle: String?,
    val requiredHours: String?,
    val availHours: String?,
    
    val `object`: String
) {
    companion object {
        fun from(program: Program, likeCount: Long, registrationCount: Long, likedByMe: Boolean, registeredByMe: Boolean, targetObject: String): ProgramDetailResponse {
            return ProgramDetailResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                targetAudience = program.targetAudience,
                programType = program.programType,
                startDate = program.startDate,
                endDate = program.endDate,
                relatedMajor = program.relatedMajor,
                price = program.price,
                imageUrl = program.imageUrl,
                eligibleRegion = program.eligibleRegion,
                venueRegion = program.venueRegion,
                operateCycle = program.operateCycle,
                interestCategoryLabel = getInterestCategoryLabel(program.interestCategory),
                likeCount = likeCount,
                registrationCount = registrationCount,
                likedByMe = likedByMe,
                registeredByMe = registeredByMe,
                requiredHours = program.programDetail?.requiredHours,
                availHours = program.programDetail?.availHours,
                `object` = targetObject
            )
        }
        
        private fun getInterestCategoryLabel(interestCategory: Int?): String? {
            return when (interestCategory) {
                1 -> "과학"
                2 -> "기술"
                4 -> "공학"
                8 -> "예술"
                16 -> "수학"
                else -> null
            }
        }
    }
}
