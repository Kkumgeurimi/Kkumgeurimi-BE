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
            ProgramSummaryResponse.from(program)
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
        val currentStudent = authService.getCurrentStudentOrNull()
        val targetObject = extractTargetObjectForStudent(program.programDetail?.levelInfo, currentStudent)
        
        return ProgramDetailResponse.from(
            program,
            stats.likeCount,
            stats.registrationCount,
            stats.likedByMe,
            stats.registeredByMe,
            targetObject
        )
    }
    
    private fun extractTargetObjectForStudent(levelInfo: String?, student: com.kkumgeurimi.kopring.domain.student.entity.Student?): String {
        if (levelInfo.isNullOrBlank()) {
            return "등록된 설명이 없습니다"
        }
        
        val studentSchoolType = student?.school?.let { school ->
            when {
                school.contains("초등학교") || school.contains("초") -> "초등학교"
                school.contains("중학교") || school.contains("중") -> "중학교"
                school.contains("고등학교") || school.contains("고") -> "고등학교"
                else -> null
            }
        }
        
        // 학생의 학교 타입이 있는 경우, 해당 타입에 맞는 설명 찾기
        if (studentSchoolType != null) {
            val lines = levelInfo.split("\n", "\r\n", "\r")
            for (line in lines) {
                if (line.contains(studentSchoolType)) {
                    // "초등학교: 목표내용" 형태에서 목표내용만 추출
                    val colonIndex = line.indexOf(":")
                    if (colonIndex != -1 && colonIndex < line.length - 1) {
                        return line.substring(colonIndex + 1).trim()
                    }
                    // 콜론이 없는 경우 학교타입 이후의 내용을 반환
                    val schoolTypeIndex = line.indexOf(studentSchoolType)
                    if (schoolTypeIndex != -1) {
                        val afterSchoolType = line.substring(schoolTypeIndex + studentSchoolType.length).trim()
                        if (afterSchoolType.isNotEmpty()) {
                            return afterSchoolType
                        }
                    }
                }
            }
        }
        
        // 학생의 학교 타입이 없거나 해당 타입에 맞는 설명이 없는 경우
        return getLongestDescription(levelInfo)
    }
    
    private fun getLongestDescription(text: String): String {
        val lines = text.split("\n", "\r\n", "\r")
        val schoolDescriptions = mutableListOf<String>()
        
        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.isEmpty()) continue
            
            // "초등학교:", "중학교:", "고등학교:" 등의 레이블 제거
            val schoolTypes = listOf("초등학교:", "중학교:", "고등학교:")
            var description = trimmedLine
            
            for (schoolType in schoolTypes) {
                if (trimmedLine.startsWith(schoolType)) {
                    description = trimmedLine.substring(schoolType.length).trim()
                    break
                }
            }
            
            if (description.isNotEmpty()) {
                schoolDescriptions.add(description)
            }
        }
        
        // 가장 긴 설명 반환
        return if (schoolDescriptions.isNotEmpty()) {
            schoolDescriptions.maxByOrNull { it.length } ?: "등록된 설명이 없습니다"
        } else {
            "등록된 설명이 없습니다"
        }
    }

    // 일주일 간의 프로그램 찜 증가량 기준으로 인기도 측정
    fun getTrendingPrograms(): List<ProgramSummaryResponse> {
        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
        val topPrograms = programRepository.findTop4ProgramsByOrderByProgramLikesInLastWeek(oneWeekAgo)
        return topPrograms.map { program ->
            ProgramSummaryResponse.from(program)
        }
    }

    fun getProgramById(programId: String): Program {
        return programRepository.findByProgramId(programId)
            ?: throw CustomException(ErrorCode.PROGRAM_NOT_FOUND)
    }
}
