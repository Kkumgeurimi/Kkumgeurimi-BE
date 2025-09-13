package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.auth.InterestCategoryRequest
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "My")
@RestController
@RequestMapping("/my")
class MyController(
    private val authService: AuthService,
    private val studentService: StudentService
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
}
