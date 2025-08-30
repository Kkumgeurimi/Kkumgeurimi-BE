package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.program.entity.*
import com.kkumgeurimi.kopring.api.dto.PageResponse
import com.kkumgeurimi.kopring.api.dto.ProgramResponse
import com.kkumgeurimi.kopring.api.dto.ProgramSearchRequest
import com.kkumgeurimi.kopring.domain.program.repository.*
import com.kkumgeurimi.kopring.domain.student.entity.Student
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProgramService(
    private val programRepository: ProgramRepository,
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val programLikeRepository: ProgramLikeRepository,
    private val authService: AuthService
) {

    @Transactional(readOnly = true)
    fun searchPrograms(request: ProgramSearchRequest): PageResponse<ProgramResponse> {
        validateSearchRequest(request)

        val pageable = PageRequest.of(request.page - 1, request.size)

        val programsPage: Page<Program> = when (request.sortBy) {
            "latest" -> programRepository.findProgramsByFiltersOrderByLatest(
                request.interestCategory,
                request.programType,
                request.cost,
                request.startDate,
                request.endDate,
                pageable
            )
            "popular" -> programRepository.findProgramsByFiltersOrderByPopular(
                request.interestCategory,
                request.programType,
                request.cost,
                request.startDate,
                request.endDate,
                pageable
            )
            "deadline" -> programRepository.findProgramsByFiltersOrderByDeadline(
                request.interestCategory,
                request.programType,
                request.cost,
                request.startDate,
                request.endDate,
                pageable
            )
            else -> throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "정렬 기준이 잘못되었습니다.")
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

    @Transactional(readOnly = true)
    fun getProgramDetail(programId: String): ProgramResponse {
        val program = getProgramById(programId)
        
        val likeCount = programLikeRepository.countByProgram(program)
        val registrationCount = programRegistrationRepository.countByProgram(program)
        
        return ProgramResponse.from(program, likeCount, registrationCount)
    }

    private fun validateSearchRequest(request: ProgramSearchRequest) {
        // 페이지 번호 검증
        if (request.page < 1) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 번호는 1 이상이어야 합니다.")
        }

        // 페이지 크기 검증
        if (request.size < 1 || request.size > 100) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "페이지 크기는 1~100 사이여야 합니다.")
        }

        // 관심 카테고리 검증
        request.interestCategory?.let { categoryId ->
            if (categoryId !in 0..31) {
                throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "관심 카테고리는 0-31 사이의 숫자여야 합니다.")
            }
        }

        // 프로그램 타입 검증
        val validProgramTypes = setOf(
            "all", "field_company", "job_practice", "field_academic",
            "subject", "camp", "lecture", "dialogue"
        )
        if (!validProgramTypes.contains(request.programType)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 프로그램 타입입니다.")
        }

        // 비용 검증
        val validCosts = setOf("all", "free", "paid")
        if (!validCosts.contains(request.cost)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 비용 필터입니다.")
        }

        // 정렬 기준 검증
        val validSortBy = setOf("latest", "popular", "deadline")
        if (!validSortBy.contains(request.sortBy)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 정렬 기준입니다.")
        }

        // 날짜 형식 검증
        request.startDate?.let { date ->
            if (!isValidDateFormat(date)) {
                throw CustomException(ErrorCode.INVALID_INPUT_FORMAT, "시작 날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)")
            }
        }

        request.endDate?.let { date ->
            if (!isValidDateFormat(date)) {
                throw CustomException(ErrorCode.INVALID_INPUT_FORMAT, "종료 날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)")
            }
        }

        // 시작 날짜와 종료 날짜 비교
        if (request.startDate != null && request.endDate != null) {
            if (request.startDate > request.endDate) {
                throw CustomException(ErrorCode.INVALID_INPUT_VALUE, "시작 날짜는 종료 날짜보다 이전이어야 합니다.")
            }
        }
    }

    private fun isValidDateFormat(date: String): Boolean {
        return try {
            val parts = date.split("-")
            if (parts.size != 3) return false

            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            year in 2000..2100 && month in 1..12 && day in 1..31
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun getProgramById(programId: String): Program {
        return programRepository.findByProgramId(programId)
            ?: throw CustomException(ErrorCode.PROGRAM_NOT_FOUND)
    }

    fun registerProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = getProgramById(programId)

        // 이미 신청한 프로그램인지 확인
        val existingRegistration = programRegistrationRepository
            .findByProgramAndStudent(program, currentStudent)

        if (existingRegistration != null) {
            throw CustomException(ErrorCode.DUPLICATE_PROGRAM_REGISTRATION)
        }

        val registration = ProgramRegistration(
            programRegistrationId = 0, // 자동 생성됨
            program = program,
            student = currentStudent,
            registrationStatus = RegistrationStatus.PENDING
        )

        programRegistrationRepository.save(registration)
    }

    fun likeProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = getProgramById(programId)

        // 이미 찜한 프로그램인지 확인
        val existingLike = programLikeRepository
            .findByProgramAndStudent(program, currentStudent)

        if (existingLike != null) {
            throw CustomException(ErrorCode.DUPLICATE_PROGRAM_LIKE)
        }

        val like = ProgramLike(
            programLikeId = 0, // 자동 생성됨
            program = program,
            student = currentStudent
        )

        programLikeRepository.save(like)
    }

    fun unlikeProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = getProgramById(programId)

        val like = programLikeRepository
            .findByProgramAndStudent(program, currentStudent)
            ?: throw CustomException(ErrorCode.ERROR)

        programLikeRepository.delete(like)
    }
}
