package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration

data class CareerMapResponse(
    val programId: String,
    val title: String,
    val description: String?,
    val interestCategory: Int?,
    val interestCategoryLabel: String?
) {
    companion object {
        fun from(registration: ProgramRegistration): CareerMapResponse {
            val program = registration.program
            val interestCategory = program.interestCategory?.let { 
                InterestCategory.fromCode(it)
            }
            
            return CareerMapResponse(
                programId = program.programId,
                title = program.programTitle,
                description = program.programDetail?.description,
                interestCategory = program.interestCategory,
                interestCategoryLabel = interestCategory?.label
            )
        }
    }
}
