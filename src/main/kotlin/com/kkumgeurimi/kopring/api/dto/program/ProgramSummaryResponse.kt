package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.VenueType
import com.kkumgeurimi.kopring.domain.program.entity.Program
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "프로그램 요약 응답 (검색용)")
data class ProgramSummaryResponse(
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
    val venueType: VenueType,
    val venueRegion: String?,
    val likeCount: Long = 0,
    val registrationCount: Long = 0,
    val likedByMe: Boolean = false,
    val registeredByMe: Boolean = false
) {
    companion object {
        fun from(program: Program, likeCount: Long, registrationCount: Long, likedByMe: Boolean, registeredByMe: Boolean): ProgramSummaryResponse {
            return ProgramSummaryResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                programType = program.programType,
                startDate = program.startDate,
                endDate = program.endDate,
                price = program.price,
                costType = program.costType,
                imageUrl = program.imageUrl,
                likeCount = likeCount,
                registrationCount = registrationCount,
                likedByMe = likedByMe,
                registeredByMe = registeredByMe,
                relatedMajor = program.relatedMajor,
                targetAudience = program.targetAudience,
                venueType = program.venueType,
                venueRegion = program.venueRegion,
            )
        }
    }
}
