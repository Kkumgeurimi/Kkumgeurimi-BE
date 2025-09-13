package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.program.entity.Program

data class MyLikedProgramResponse(
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val imageUrl: String?
) {
    companion object {
        fun from(program: Program): MyLikedProgramResponse {
            return MyLikedProgramResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                imageUrl = program.imageUrl
            )
        }
    }
}
