package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.common.ProgramType
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import java.time.LocalDate

data class MyProgramWithRegistrationStatusResponse(
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
    val registrationStatus: RegistrationStatus?
) {
    companion object {
        fun from(registration: ProgramRegistration): MyProgramWithRegistrationStatusResponse {
            val program = registration.program

            val interestCategoryLabel = program.interestCategory?.let {
                InterestCategory.fromCode(it).label
            }

            val programTypeLabel = program.programType.let {
                ProgramType.fromCode(it)?.label
            }

            return MyProgramWithRegistrationStatusResponse(
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
                registrationStatus = registration.registrationStatus
            )
        }
    }
}
