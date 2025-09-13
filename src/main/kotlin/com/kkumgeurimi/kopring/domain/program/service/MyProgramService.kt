
package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.program.MyLikedProgramResponse
import com.kkumgeurimi.kopring.api.dto.program.MyProgramResponse
import com.kkumgeurimi.kopring.api.dto.program.MyUpcomingProgramResponse
import com.kkumgeurimi.kopring.api.dto.student.MyStudentProfileResponse
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.repository.ProgramLikeRepository
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class MyProgramService(
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val programLikeRepository: ProgramLikeRepository,
    private val authService: AuthService
) {
    @Transactional(readOnly = true)
    fun getMyPrograms(status: RegistrationStatus?): List<MyProgramResponse> {
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
        return registrations.map { MyProgramResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getMyLikedPrograms(): List<MyLikedProgramResponse> {
        val currentStudent = authService.getCurrentStudent()
        val likedPrograms = programLikeRepository.findByStudentWithProgram(currentStudent)
        return likedPrograms.map { MyLikedProgramResponse.from(it.program) }
    }

    @Transactional(readOnly = true)
    fun getMyUpcomingPrograms(): List<MyUpcomingProgramResponse> {
        val currentStudent = authService.getCurrentStudent()
        // REGISTERED 상태를 명시적으로 전달
        val upcomingPrograms = programRegistrationRepository.findByStudentAndProgramStartDateAfterAndRegistrationStatusWithProgram(
            currentStudent,
            LocalDate.now(),
            RegistrationStatus.REGISTERED
        )
        return upcomingPrograms.map { MyUpcomingProgramResponse.from(it.program) }
    }

    @Transactional(readOnly = true)
    fun getMyStudentProfile(): MyStudentProfileResponse {
        val currentStudent = authService.getCurrentStudent()
        return MyStudentProfileResponse.from(currentStudent)
    }
}