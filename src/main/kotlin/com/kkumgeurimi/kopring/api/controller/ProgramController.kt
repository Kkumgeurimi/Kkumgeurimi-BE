package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.PageResponse
import com.kkumgeurimi.kopring.api.dto.ProgramResponse
import com.kkumgeurimi.kopring.api.dto.ProgramSearchRequest
import com.kkumgeurimi.kopring.domain.program.service.ProgramService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Program")
@RestController
@RequestMapping("/programs")
class ProgramController(
    private val programService: ProgramService
) {

    @Operation(summary = "프로그램 검색", description = "다양한 필터와 정렬 옵션을 사용하여 프로그램을 검색합니다.")
    @GetMapping("/search")
    fun searchPrograms(
        @Parameter(description = "관심 카테고리 (직무/전공) - 0-31", example = "0")
        @RequestParam(required = false) interestCategory: String?,
        
        @Parameter(description = "체험유형", example = "field_company")
        @RequestParam(defaultValue = "all") programType: String,
        
        @Parameter(description = "비용", example = "free")
        @RequestParam(defaultValue = "all") cost: String,
        
        @Parameter(description = "시작 날짜", example = "2025-09-01")
        @RequestParam(required = false) startDate: String?,
        
        @Parameter(description = "종료 날짜", example = "2025-12-31")
        @RequestParam(required = false) endDate: String?,
        
        @Parameter(description = "정렬 기준", example = "latest")
        @RequestParam(defaultValue = "latest") sortBy: String,
        
        @Parameter(description = "페이지 번호", example = "1")
        @RequestParam(defaultValue = "1") page: Int,
        
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") size: Int
    ): PageResponse<ProgramResponse> {
        val request = ProgramSearchRequest(
            interestCategory = interestCategory,
            programType = programType,
            cost = cost,
            startDate = startDate,
            endDate = endDate,
            sortBy = sortBy,
            page = page,
            size = size
        )
        
        return programService.searchPrograms(request)
    }
}
