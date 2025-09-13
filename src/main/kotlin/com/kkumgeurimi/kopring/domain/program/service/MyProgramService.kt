package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.program.MyProgramResponse
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MyProgramService(
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val authService: AuthService
) {
    
    @Transactional(readOnly = true)
    fun getMyPrograms(status: RegistrationStatus?): List<MyProgramResponse> {
        val currentStudent = authService.getCurrentStudent()
        
        val registrations = when (status) {
            RegistrationStatus.REGISTERED -> {
                // 참여중인 프로그램
                programRegistrationRepository.findByStudentAndStatus(currentStudent, RegistrationStatus.REGISTERED)
            }
            RegistrationStatus.COMPLETED -> {
                // 완료된 프로그램
                programRegistrationRepository.findByStudentAndStatus(currentStudent, RegistrationStatus.COMPLETED)
            }
            null -> {
                // 전체 프로그램
                programRegistrationRepository.findByStudentOrderByCreatedAtDesc(currentStudent)
            }
        }
        
        return registrations.map { MyProgramResponse.from(it) }
    }
}
