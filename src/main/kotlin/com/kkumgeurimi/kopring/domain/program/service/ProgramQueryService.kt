package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.dto.PageResponse
import com.kkumgeurimi.kopring.api.dto.ProgramResponse
import com.kkumgeurimi.kopring.api.dto.ProgramSearchRequest
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.common.SortBy
import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.repository.ProgramLikeRepository
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProgramQueryService(
    private val programRepository: ProgramRepository,
    private val programLikeRepository: ProgramLikeRepository,
    private val programRegistrationRepository: ProgramRegistrationRepository
) {
    fun searchPrograms(request: ProgramSearchRequest): PageResponse<ProgramResponse> {
        if (request.page < 1) throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 번호는 1 이상이어야 합니다.")
        if (request.size !in 1..100) throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 크기는 1~100 사이여야 합니다.")

        if (request.startDate != null && request.endDate != null && request.startDate.isAfter(request.endDate)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "시작 날짜는 종료 날짜보다 이전이어야 합니다.")
        }

        val pageable = PageRequest.of(request.page - 1, request.size)

        val programsPage = when (request.sortBy) {
            SortBy.LATEST -> programRepository.findProgramsByFiltersOrderByLatest(
                request.interestCategory, request.programType, request.cost,
                request.startDate, request.endDate, pageable
            )
            SortBy.POPULAR -> programRepository.findProgramsByFiltersOrderByPopular(
                request.interestCategory, request.programType, request.cost,
                request.startDate, request.endDate, pageable
            )
            SortBy.DEADLINE -> programRepository.findProgramsByFiltersOrderByDeadline(
                request.interestCategory, request.programType, request.cost,
                request.startDate, request.endDate, pageable
            )
        }

        val programResponses = programsPage.content.map { program ->
            val likeCount = programLikeRepository.countByProgram(program)
            val registrationCount = programRegistrationRepository.countByProgram(program)
            ProgramResponse.from(program, likeCount, registrationCount)
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

    fun getProgramDetail(programId: String): ProgramResponse {
        val program = getProgramById(programId)
        val likeCount = programLikeRepository.countByProgram(program)
        val registrationCount = programRegistrationRepository.countByProgram(program)
        return ProgramResponse.from(program, likeCount, registrationCount)
    }

    fun getProgramById(programId: String): Program {
        return programRepository.findByProgramId(programId)
            ?: throw CustomException(ErrorCode.PROGRAM_NOT_FOUND)
    }
}
