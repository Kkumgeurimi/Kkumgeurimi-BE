package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ProgramRegistrationService (
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val authService: AuthService,
    private val programQueryService: ProgramQueryService
) {
    fun registerProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = programQueryService.getProgramById(programId)

        val existingRegistration = programRegistrationRepository.findByProgramAndStudent(program, currentStudent)
        if (existingRegistration != null) {
            throw CustomException(ErrorCode.DUPLICATE_PROGRAM_REGISTRATION)
        }
        val registration = ProgramRegistration(
            programRegistrationId = 0,
            program = program,
            student = currentStudent,
            registrationStatus = RegistrationStatus.REGISTERED
        )
        programRegistrationRepository.save(registration)
    }
}