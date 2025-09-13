package com.kkumgeurimi.kopring.domain.career.service

import com.kkumgeurimi.kopring.api.dto.career.CareerAnalysisResponse
import com.kkumgeurimi.kopring.domain.career.entity.CareerAnalysis
import com.kkumgeurimi.kopring.domain.career.repository.CareerAnalysisRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CareerAnalysisService(
    private val careerAnalysisRepository: CareerAnalysisRepository,
    private val authService: AuthService
) {

    @Transactional(readOnly = true)
    fun getCurrentStudentCareerAnalysis(): CareerAnalysisResponse? {
        val currentStudent = authService.getCurrentStudent()
        val analysis = careerAnalysisRepository.findTopByStudentOrderByCreatedAtDesc(currentStudent)
        
        return analysis?.let { CareerAnalysisResponse.from(it) }
    }
}
