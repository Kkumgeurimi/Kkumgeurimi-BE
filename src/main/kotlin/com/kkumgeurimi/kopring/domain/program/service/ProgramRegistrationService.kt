package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.program.MyProgramWithRegistrationStatusResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramSummaryResponse
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


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

    @Transactional(readOnly = true)
    fun getMyPrograms(status: RegistrationStatus?): List<MyProgramWithRegistrationStatusResponse> {
        val currentStudent = authService.getCurrentStudent()
        val registrations = when (status) {
            RegistrationStatus.REGISTERED -> {
                programRegistrationRepository.findByStudentAndStatusWithProgramOrderByCreatedAtDesc(currentStudent, RegistrationStatus.REGISTERED)
            }
            RegistrationStatus.COMPLETED -> {
                programRegistrationRepository.findByStudentAndStatusWithProgramOrderByCreatedAtDesc(currentStudent, RegistrationStatus.COMPLETED)
            }
            null -> {
                programRegistrationRepository.findByStudentWithProgramOrderByCreatedAtDesc(currentStudent)
            }
        }
        return registrations.map { MyProgramWithRegistrationStatusResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getMyUpcomingPrograms(): List<ProgramSummaryResponse> {
        val currentStudent = authService.getCurrentStudent()
        val upcomingPrograms = programRegistrationRepository.findByStudentAndProgramStartDateAfterAndRegistrationStatusWithProgram(
            currentStudent,
            LocalDate.now(),
            RegistrationStatus.REGISTERED
        )
        return upcomingPrograms.map { ProgramSummaryResponse.from(it.program) }
    }
}