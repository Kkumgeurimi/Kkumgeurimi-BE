package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.common.PageResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramDetailResponse
import com.kkumgeurimi.kopring.api.dto.program.ProgramSearchRequest
import com.kkumgeurimi.kopring.api.dto.program.ProgramSummaryResponse
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.common.SortBy
import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class ProgramQueryService(
    private val programRepository: ProgramRepository,
    private val programStatisticsService: ProgramStatisticsService,
    private val authService: AuthService
) {
    fun searchPrograms(request: ProgramSearchRequest): PageResponse<ProgramSummaryResponse> {
        if (request.page < 1) throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 번호는 1 이상이어야 합니다.")
        if (request.size !in 1..100) throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 크기는 1~100 사이여야 합니다.")
        // null 방지 위해 기본값 설정
        val startDate = request.startDate ?: LocalDate.now()
        val endDate = request.endDate ?: LocalDate.now().plusYears(1)
        val currentStudentOrNull = authService.getCurrentStudentOrNull()
        val effectiveTargetAudience = request.targetAudience ?: currentStudentOrNull?.school?.let {
            when {
                it.contains("초") -> "초"
                it.contains("중") -> "중"
                it.contains("고") -> "고"
                else -> null
            }
        }
        if (startDate.isAfter(endDate)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "시작 날짜는 종료 날짜보다 이전이어야 합니다.")
        }
        if (java.time.Period.between(startDate, endDate).years >= 2 ||
            java.time.Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() > 731) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "최대 조회 간격은 2년입니다.")
        }
        val pageable = PageRequest.of(request.page - 1, request.size)
        val programsPage = when (request.sortBy) {
            SortBy.LATEST -> programRepository.findProgramsByFiltersOrderByLatest(
                request.interestCategory, request.programType, request.costType,
                effectiveTargetAudience, request.keyword,
                startDate, endDate, pageable
            )
            SortBy.POPULAR -> programRepository.findProgramsByFiltersOrderByPopular(
                request.interestCategory, request.programType, request.costType,
                effectiveTargetAudience, request.keyword,
                startDate, endDate, pageable
            )
            SortBy.DEADLINE -> programRepository.findProgramsByFiltersOrderByDeadline(
                request.interestCategory, request.programType, request.costType,
                effectiveTargetAudience, request.keyword,
                startDate, endDate, pageable
            )
        }
        val programResponses = programsPage.content.map { program ->
            val stats = programStatisticsService.getProgramStats(program)
            ProgramSummaryResponse.from(
                program,
                stats.likeCount,
                stats.registrationCount,
                stats.likedByMe,
                stats.registeredByMe
            )
        }
        return PageResponse(
            content = programResponses,
            pageNumber = programsPage.number + 1,
            pageSize = programsPage.size,
            totalElements = programsPage.totalElements,
            totalPages = programsPage.totalPages,
            hasNext = programsPage.hasNext(),
            hasPrevious = programsPage.hasPrevious()
        )
    }

    fun getProgramDetail(programId: String): ProgramDetailResponse {
        val program = getProgramById(programId)
        val stats = programStatisticsService.getProgramStats(program)
        return ProgramDetailResponse.from(
            program,
            stats.likeCount,
            stats.registrationCount,
            stats.likedByMe,
            stats.registeredByMe
        )
    }

    // 일주일 간의 프로그램 찜 증가량 기준으로 인기도 측정
    fun getTrendingPrograms(): List<ProgramSummaryResponse> {
        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
        val topPrograms = programRepository.findTop4ProgramsByOrderByProgramLikesInLastWeek(oneWeekAgo)
        return topPrograms.map { program ->
            val stats = programStatisticsService.getProgramStats(program)
            ProgramSummaryResponse.from(
                program,
                stats.likeCount,
                stats.registrationCount,
                stats.likedByMe,
                stats.registeredByMe
            )
        }
    }

    fun getProgramById(programId: String): Program {
        return programRepository.findByProgramId(programId)
            ?: throw CustomException(ErrorCode.PROGRAM_NOT_FOUND)
    }
}
