package com.kkumgeurimi.kopring.api.dto.program

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.SortBy
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDate

@Schema(description = "프로그램 검색 요청")
data class ProgramSearchRequest(
    @Schema(description = "관심 카테고리 (0-31)", example = "0")
    @field:Min(0)
    @field:Max(31)
    val interestCategory: Int? = null,

    @Schema(description = "체험유형 코드 (0-6)", example = "0")
    @field:Min(0)
    @field:Max(6)
    val programType: Int? = null,

    @Schema(description = "비용")
    val costType: CostType? = null,

    @Schema(description = "시작 날짜")
    val startDate: LocalDate? = null,

    @Schema(description = "종료 날짜")
    val endDate: LocalDate? = null,

    @Schema(description = "정렬 기준", example = "LATEST")
    val sortBy: SortBy = SortBy.LATEST,

    @Schema(description = "페이지 번호", example = "1")
    val page: Int = 1,

    @Schema(description = "페이지 크기", example = "10")
    val size: Int = 10,

    @Schema(description = "대상 학교 (초/중/고)")
    val targetAudience: String = "",

    @Schema(description = "검색 키워드 (프로그램 제목/관련 전공)")
    val keyword: String = ""
)