package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.auth.StudentResponse
import com.kkumgeurimi.kopring.domain.student.entity.toResponse
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Student")
@RestController
@RequestMapping("/students")
class StudentController(
    private val studentService: StudentService
) {
    
    @Operation(summary = "학생 정보 조회")
    @GetMapping("/{id}")
    fun getStudent(@PathVariable id: Long): StudentResponse {
        return studentService.findById(id).toResponse()
    }
}
