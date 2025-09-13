package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.common.PageResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramDetailResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramSearchRequest
import com.kkumgeurimi.kopring.api.dto.program.ProgramSummaryResponse
import com.kkumgeurimi.kopring.domain.program.service.ProgramLikeService
import com.kkumgeurimi.kopring.domain.program.service.ProgramQueryService
import com.kkumgeurimi.kopring.domain.program.service.ProgramRegistrationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Program")
@RestController
@RequestMapping("/programs")
class ProgramController(
    private val programQueryService: ProgramQueryService,
    private val programRegistrationService: ProgramRegistrationService,
    private val programLikeService: ProgramLikeService,
) {
    @Operation(summary = "프로그램 검색", description = "다양한 필터와 정렬 옵션을 사용하여 프로그램을 검색합니다.")
    @GetMapping("/search")
    fun searchPrograms(
        request: ProgramSearchRequest
    ): PageResponse<ProgramSummaryResponse> {
        return programQueryService.searchPrograms(request)
    }

    @Operation(summary = "프로그램 상세 조회")
    @GetMapping("/{program_id}")
    fun getProgramDetail(
        @PathVariable("program_id") programId: String
    ): ProgramDetailResponse {
        return programQueryService.getProgramDetail(programId)
    }

    @Operation(summary = "근 일주일간의 인기 프로그램 4개 조회")
    @GetMapping("/trending")
    fun getTrendingPrograms(
    ): List<ProgramSummaryResponse> {
        return programQueryService.getTrendingPrograms()
    }

    @Operation(summary = "프로그램 신청")
    @PostMapping("/{program_id}/register")
    fun registerProgram(
        @PathVariable("program_id") programId: String
    ) {
        programRegistrationService.registerProgram(programId)
    }

    @Operation(summary = "프로그램 찜하기")
    @PostMapping("/{program_id}/like")
    fun likeProgram(
        @PathVariable("program_id") programId: String
    ) {
        programLikeService.likeProgram(programId)
    }

    @Operation(summary = "프로그램 찜 삭제")
    @DeleteMapping("/{program_id}/like")
    fun unlikeProgram(
        @PathVariable("program_id") programId: String
    ) {
        programLikeService.unlikeProgram(programId)
    }
}
