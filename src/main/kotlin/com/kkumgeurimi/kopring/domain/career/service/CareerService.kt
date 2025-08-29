package com.kkumgeurimi.kopring.domain.career.service

import com.kkumgeurimi.kopring.api.dto.CareerMapResponse
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CareerService(
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val authService: AuthService
) {

    @Transactional(readOnly = true)
    fun getCurrentStudentCareerMap(): List<CareerMapResponse> {
        val currentStudent = authService.getCurrentStudent()
        
        // 현재 학생이 신청한 프로그램의 최신 20개를 조회
        val registrations = programRegistrationRepository
            .findByStudentOrderByCreatedAtDesc(currentStudent)
            .take(20)
        
        return registrations.map { registration ->
            CareerMapResponse.from(registration)
        }
    }

}
