package com.kkumgeurimi.kopring.domain.student.service

import com.kkumgeurimi.kopring.api.dto.auth.StudentSignUpRequest
import com.kkumgeurimi.kopring.api.dto.student.MyStudentProfileResponse
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.student.entity.Student
import com.kkumgeurimi.kopring.domain.student.repository.StudentRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StudentService(
    private val studentRepository: StudentRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authService: AuthService,
    private val nicknameGenerator: NicknameGenerator
) {
    
    fun signUp(request: StudentSignUpRequest): Student {
        
        // 이메일 중복 확인
        if (studentRepository.existsByEmail(request.email)) {
            throw CustomException(ErrorCode.DUPLICATE_EMAIL)
        }
        
        // 학생 엔티티 생성 및 저장
        val student = Student(
            email = request.email,
            name = request.name,
            phone = request.phone,
            address = request.address,
            imageUrl = request.imageUrl,
            birth = request.birth,
            school = request.school,
            interestCategory = request.interestCategory,
            career = request.career,
            password = passwordEncoder.encode(request.password),
            nickname = nicknameGenerator.generateUnique()
        )
        
        return studentRepository.save(student)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Student {
        return studentRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.STUDENT_NOT_FOUND) }
    }
    
    @Transactional(readOnly = true)
    fun findByEmail(email: String): Student {
        return studentRepository.findByEmail(email)
            ?: throw CustomException(ErrorCode.STUDENT_NOT_FOUND)
    }
    @Transactional(readOnly = true)
    fun findByEmailOrNull(email: String): Student? {
        return studentRepository.findByEmail(email)
    }
    
    fun updateStudentInterestCategory(email: String, interestCategory: Int): Student {
        val student = findByEmail(email)
        student.interestCategory = interestCategory
        return studentRepository.save(student)
    }

    @Transactional(readOnly = true)
    fun getMyStudentProfile(): MyStudentProfileResponse {
        val currentStudent = authService.getCurrentStudent()
        return MyStudentProfileResponse.from(currentStudent)
    }

    @Transactional(readOnly = true)
    fun getMySchoolLevel(): String {
        return authService.getCurrentStudent().getSchoolLevel()
    }
}
