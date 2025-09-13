package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.review.MyReviewResponse
import com.kkumgeurimi.kopring.api.dto.review.ReviewCreateRequest
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.entity.Student
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService(
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val authService: AuthService
) {
    // 리뷰 작성
    fun createReview(programRegistrationId: Long, request: ReviewCreateRequest) {
        val currentStudent = authService.getCurrentStudent()
        val registration = programRegistrationRepository.findById(programRegistrationId)
            .orElseThrow { CustomException(ErrorCode.PROGRAM_REGISTRATION_NOT_FOUND) }
        
        validateOwnership(registration, currentStudent)
        
        // 프로그램이 완료되었는지 확인
        if (!registration.isProgramCompleted()) {
            throw CustomException(ErrorCode.PROGRAM_NOT_COMPLETED)
        }
        
        // 이미 리뷰가 있는지 확인 (점수가 필수이므로 점수로만 확인)
        if (registration.reviewScore != null) {
            throw CustomException(ErrorCode.REVIEW_ALREADY_EXISTS)
        }
        
        val updatedRegistration = ProgramRegistration(
            programRegistrationId = registration.programRegistrationId,
            program = registration.program,
            student = registration.student,
            registrationStatus = registration.registrationStatus,
            reviewScore = request.reviewScore, 
            reviewMessage = request.reviewMessage?.takeIf { it.isNotBlank() } // 선택사항
        )
        
        programRegistrationRepository.save(updatedRegistration)
    }
    
    // 리뷰 삭제
    fun deleteReview(programRegistrationId: Long) {
        val currentStudent = authService.getCurrentStudent()
        val registration = programRegistrationRepository.findById(programRegistrationId)
            .orElseThrow { CustomException(ErrorCode.PROGRAM_REGISTRATION_NOT_FOUND) }
        
        validateOwnership(registration, currentStudent)
        
        // 리뷰가 있는지 확인 (점수가 필수이므로 점수로만 확인)
        if (registration.reviewScore == null) {
            throw CustomException(ErrorCode.REVIEW_NOT_FOUND)
        }
        
        // 새로운 ProgramRegistration 객체 생성 (리뷰 정보 제거)
        val updatedRegistration = ProgramRegistration(
            programRegistrationId = registration.programRegistrationId,
            program = registration.program,
            student = registration.student,
            registrationStatus = registration.registrationStatus,
            reviewScore = null,
            reviewMessage = null
        )
        
        programRegistrationRepository.save(updatedRegistration)
    }
    
    // 로그인된 유저가 작성한 리뷰 조회
    @Transactional(readOnly = true)
    fun getMyReviews(): List<MyReviewResponse> {
        val currentStudent = authService.getCurrentStudent()
        val reviewRegistrations = programRegistrationRepository.findReviewsByStudent(currentStudent)
        
        return reviewRegistrations.map { MyReviewResponse.from(it) }
    }
    
    // 생성자 검증
    private fun validateOwnership(registration: ProgramRegistration, currentStudent: Student) {
        if (registration.student.studentId != currentStudent.studentId) {
            throw CustomException(ErrorCode.UNAUTHORIZED_ACCESS)
        }
    }
}
