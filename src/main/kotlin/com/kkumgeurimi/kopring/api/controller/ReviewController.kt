package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.review.ReviewCreateRequest
import com.kkumgeurimi.kopring.domain.program.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@Tag(name = "Review")
@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @Operation(summary = "리뷰 작성", description = "완료된 프로그램에 대해 리뷰를 작성합니다.")
    @PostMapping("/reviews/{program_registration_id}")
    fun createReview(
        @PathVariable("program_registration_id") programRegistrationId: Long,
        @Valid @RequestBody request: ReviewCreateRequest
    ): ResponseEntity<Void> {
        reviewService.createReview(programRegistrationId, request)
        return ResponseEntity.ok().build()
    }
    
    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/reviews/{program_registration_id}")
    fun deleteReview(
        @PathVariable("program_registration_id") programRegistrationId: Long
    ): ResponseEntity<Void> {
        reviewService.deleteReview(programRegistrationId)
        return ResponseEntity.ok().build()
    }
    
}
