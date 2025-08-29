package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import java.time.format.DateTimeFormatter

data class CareerMapResponse(
    val programId: String,
    val title: String,
    val description: String?,
    val interestCategoryId: Int?
) {
    companion object {
        fun from(registration: ProgramRegistration): CareerMapResponse {
            val program = registration.program
            
            return CareerMapResponse(
                programId = program.programId,
                title = program.programTitle,
                description = program.programDetail?.description,
                interestCategoryId = program.interestCategoryId
            )
        }
    }
}
