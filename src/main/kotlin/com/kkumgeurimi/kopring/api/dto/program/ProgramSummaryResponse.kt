package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.common.ProgramType
import com.kkumgeurimi.kopring.domain.program.entity.Program
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "프로그램 요약 응답 (검색용)")
data class ProgramSummaryResponse(
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val programTypeLabel: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val costType: CostType?,
    val imageUrl: String?,
    val venueRegion: String?,
    val interestCategoryLabel: String?,
) {
    companion object {
        fun from(program: Program): ProgramSummaryResponse {
            val interestCategoryLabel = program.interestCategory?.let { 
                InterestCategory.fromCode(it)?.label
            }
            
            val programTypeLabel = program.programType?.let { 
                ProgramType.fromCode(it)?.label
            }
            
            return ProgramSummaryResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                programTypeLabel = programTypeLabel,
                startDate = program.startDate,
                endDate = program.endDate,
                costType = program.costType,
                imageUrl = program.imageUrl,
                venueRegion = program.venueRegion,
                interestCategoryLabel = interestCategoryLabel,
            )
        }
    }
}
