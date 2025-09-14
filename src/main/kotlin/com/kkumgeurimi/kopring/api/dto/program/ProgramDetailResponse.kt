package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.CostType
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
    val costType: CostType?,
    val imageUrl: String?,
    val venueRegion: String?,
    val likeCount: Long = 0,
    val registrationCount: Long = 0,
    val likedByMe: Boolean = false,
    val registeredByMe: Boolean = false,

    // 상세 전용 필드들
    val eligibleRegion: String?,
    val interestCategory: Int?,
    val operateCycle: String?,
    val description: String?,
    val requiredHours: String?,
    val availHours: String?,
    val capacity: Int?,
    val targetSchoolType: String?,
    val levelInfo: String?
) {
    companion object {
        fun from(program: Program, likeCount: Long, registrationCount: Long, likedByMe: Boolean, registeredByMe: Boolean): ProgramDetailResponse {
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
                costType = program.costType,
                imageUrl = program.imageUrl,
                eligibleRegion = program.eligibleRegion,
                venueRegion = program.venueRegion,
                operateCycle = program.operateCycle,
                interestCategory = program.interestCategory,
                likeCount = likeCount,
                registrationCount = registrationCount,
                likedByMe = likedByMe,
                registeredByMe = registeredByMe,
                description = program.programDetail?.description,
                requiredHours = program.programDetail?.requiredHours,
                availHours = program.programDetail?.availHours,
                capacity = program.programDetail?.capacity,
                targetSchoolType = program.programDetail?.targetSchoolType,
                levelInfo = program.programDetail?.levelInfo
            )
        }
    }
}
