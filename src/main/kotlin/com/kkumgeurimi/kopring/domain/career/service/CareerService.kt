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
        
        // 현재 학생이 신청한 프로그램들을 조회
        val registrations = programRegistrationRepository
            .findByStudentOrderByCreatedAtDesc(currentStudent)
        
        // 관심 카테고리별로 그룹화하고 개수 계산
        val categoryCountMap = registrations
            .mapNotNull { it.program.interestCategoryId }
            .groupingBy { it }
            .eachCount()
        
        // 관심 카테고리별로 프로그램들을 그룹화
        val programsByCategory = registrations
            .groupBy { it.program.interestCategoryId }
            .filterKeys { it != null }
            .mapKeys { it.key!! }
        
        // 카테고리별 개수가 많은 순으로 정렬하고 상위 10개 선택
        val topCategories = categoryCountMap
            .toList()
            .sortedByDescending { it.second }
            .take(10)
            .takeIf { it.isNotEmpty() } ?: emptyList()
        
        // 선택된 카테고리들에서 프로그램들을 가져와서 응답 생성
        val result = mutableListOf<CareerMapResponse>()
        
        for ((categoryId, count) in topCategories) {
            val categoryPrograms = programsByCategory[categoryId] ?: continue
            
            // 각 카테고리에서 실제 참여한 모든 프로그램을 최신순으로 선택
            val selectedPrograms = categoryPrograms
                .sortedByDescending { it.createdAt }
            
            result.addAll(selectedPrograms.map { CareerMapResponse.from(it) })
            
            // 10개를 넘지 않도록 제한
            if (result.size >= 10) break
        }
        
        return result.take(10)
    }

}
