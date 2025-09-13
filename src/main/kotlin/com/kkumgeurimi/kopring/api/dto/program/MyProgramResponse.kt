package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus

data class MyProgramResponse(
    val programRegistrationId: Long,
    val programId: String,
    val programTitle: String,
    val provider: String?,
    val imageUrl: String?,
    val registrationStatus: RegistrationStatus
) {
    companion object {
        fun from(registration: ProgramRegistration): MyProgramResponse {
            return MyProgramResponse(
                programRegistrationId = registration.programRegistrationId,
                programId = registration.program.programId,
                programTitle = registration.program.programTitle,
                provider = registration.program.provider,
                imageUrl = registration.program.imageUrl,
                registrationStatus = registration.registrationStatus
            )
        }
    }
}
