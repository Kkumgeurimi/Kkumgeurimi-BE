package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.program.entity.Program
import java.time.LocalDate

data class MyUpcomingProgramResponse(
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val imageUrl: String?,
    val venueRegion: String?
) {
    companion object {
        fun from(program: Program): MyUpcomingProgramResponse {
            return MyUpcomingProgramResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                startDate = program.startDate,
                endDate = program.endDate,
                imageUrl = program.imageUrl,
                venueRegion = program.venueRegion
            )
        }
    }
}
