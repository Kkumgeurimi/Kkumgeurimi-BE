package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.common.ProgramType
import com.kkumgeurimi.kopring.domain.common.SortBy
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "프로그램 검색 요청")
data class ProgramSearchRequest(
    @Schema(description = "관심 카테고리 (직무/전공) - 0-31", example = "0")
    val interestCategory: InterestCategory? = null,

    @Schema(description = "체험유형", example = "field_company")
    val programType: ProgramType = ProgramType.ALL,

    @Schema(description = "비용", example = "free")
    val cost: CostType = CostType.ALL,

    @Schema(description = "시작 날짜", example = "2025-09-01")
    val startDate: LocalDate? = null,

    @Schema(description = "종료 날짜", example = "2025-12-31")
    val endDate: LocalDate? = null,

    @Schema(description = "정렬 기준", example = "LATEST")
    val sortBy: SortBy = SortBy.LATEST,

    @Schema(description = "페이지 번호", example = "1")
    val page: Int = 1,

    @Schema(description = "페이지 크기", example = "10")
    val size: Int = 10
) {}
