package com.kkumgeurimi.kopring.api.dto

import InterestCategory
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration

data class CareerMapResponse(
    val programId: String,
    val title: String,
    val description: String?,
    val interestCategoryId: Int?,
    val interestCategoryLabel: String?
) {
    companion object {
        fun from(registration: ProgramRegistration): CareerMapResponse {
            val program = registration.program
            val interestCategory = program.interestCategoryId?.let { 
                InterestCategory.fromCode(it) 
            }
            
            return CareerMapResponse(
                programId = program.programId,
                title = program.programTitle,
                description = program.programDetail?.description,
                interestCategoryId = program.interestCategoryId,
                interestCategoryLabel = interestCategory?.label
            )
        }
    }
}
