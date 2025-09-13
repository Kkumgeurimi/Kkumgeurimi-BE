package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.auth.InterestCategoryRequest
import com.kkumgeurimi.kopring.api.dto.program.MyLikedProgramResponse
import com.kkumgeurimi.kopring.api.dto.student.MyStudentProfileResponse
import com.kkumgeurimi.kopring.api.dto.program.MyProgramResponse
import com.kkumgeurimi.kopring.api.dto.program.MyUpcomingProgramResponse
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.service.MyProgramService
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "My")
@RestController
@RequestMapping("/my")
class MyController(
    private val authService: AuthService,
    private val studentService: StudentService,
    private val myProgramService: MyProgramService
) {

    @Operation(summary = "현재 유저(학생)의 관심사 설정")
    @PostMapping("/interests")
    fun updateInterestCategory(
        @Valid @RequestBody request: InterestCategoryRequest
    ): ResponseEntity<Unit> {
        val currentStudent = authService.getCurrentStudent()
        studentService.updateStudentInterestCategory(currentStudent.email, request.interestCategory)
        return ResponseEntity.ok().build()
    }
    
    @Operation(
        summary = "내가 참여중인/완료한 프로그램 목록 조회", 
        description = "status 파라미터로 참여중/완료 상태를 필터링할 수 있습니다."
    )
    @GetMapping("/programs")
    fun getMyPrograms(
        @Parameter(description = "프로그램 상태 (REGISTERED: 참여중, COMPLETED: 완료, null: 전체)")
        @RequestParam(required = false) status: RegistrationStatus?
    ): List<MyProgramResponse> {
        return myProgramService.getMyPrograms(status)
    }

    @GetMapping("/upcoming")
    fun getMyUpcomingPrograms(
    ): List<MyUpcomingProgramResponse> {
        return myProgramService.getMyUpcomingPrograms()
    }

    @GetMapping("/likes")
    fun getMyLikedPrograms(
    ): List<MyLikedProgramResponse> {
        return myProgramService.getMyLikedPrograms()
    }

    @GetMapping
    fun getMyProfile(
    ): MyStudentProfileResponse {
        return myProgramService.getMyStudentProfile()
    }
}
