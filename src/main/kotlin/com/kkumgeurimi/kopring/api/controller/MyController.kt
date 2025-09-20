package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.auth.InterestCategoryRequest
import com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse
import com.kkumgeurimi.kopring.api.dto.review.MyReviewResponse
import com.kkumgeurimi.kopring.api.dto.student.MyStudentProfileResponse
import com.kkumgeurimi.kopring.api.dto.program.MyProgramWithRegistrationStatusResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramSummaryResponse
import com.kkumgeurimi.kopring.domain.community.service.PostService
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.service.ProgramLikeService
import com.kkumgeurimi.kopring.domain.program.service.ProgramRegistrationService
import com.kkumgeurimi.kopring.domain.program.service.ReviewService
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import com.kkumgeurimi.kopring.domain.student.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "My")
@RestController
@RequestMapping("/my")
class MyController(
    private val authService: AuthService,
    private val studentService: StudentService,
    private val programRegistrationService: ProgramRegistrationService,
    private val programLikeService: ProgramLikeService,
    private val reviewService: ReviewService,
    private val postService: PostService
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
        summary = "내가 완료한 프로그램 목록 조회", 
        description = "완료한 프로그램 목록을 조회합니다."
    )
    @GetMapping("/completed")
    fun getMyCompletedPrograms(
    ): List<MyProgramWithRegistrationStatusResponse> {
        return programRegistrationService.getMyCompletedPrograms()
    }

    @Operation(summary = "내가 참여 예정인 프로그램 목록 조회")
    @GetMapping("/upcoming")
    fun getMyUpcomingPrograms(
    ): List<ProgramSummaryResponse> {
        return programRegistrationService.getMyUpcomingPrograms()
    }

    @Operation(summary = "내가 찜한 프로그램 목록 조회")
    @GetMapping("/likes")
    fun getMyLikedPrograms(
    ): List<ProgramSummaryResponse> {
        return programLikeService.getMyLikedPrograms()
    }

    @Operation(summary = "내 프로필 조회")
    @GetMapping
    fun getMyProfile(
    ): MyStudentProfileResponse {
        return studentService.getMyStudentProfile()
    }

    @Operation(summary = "내 학교 레벨 조회")
    @GetMapping
    fun getMySchoolLevel(
    ): String {
        return studentService.getMySchoolLevel()
    }

    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "현재 사용자가 작성한 모든 리뷰 목록을 조회합니다.")
    @GetMapping("/reviews")
    fun getMyReviews(): List<MyReviewResponse> {
        return reviewService.getMyReviews()
    }

    @Operation(summary = "내가 작성한 게시글 목록 조회")
    @GetMapping("/posts")
    fun getMyPosts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<PostSummaryResponse> {
        return postService.getMyPosts(page-1, size)
    }

    @Operation(summary = "내가 공감한 게시글 목록 조회")
    @GetMapping("/posts/likes")
    fun getMyLikedPosts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<PostSummaryResponse> {
        return postService.getMyLikedPosts(page-1, size)
    }
}
