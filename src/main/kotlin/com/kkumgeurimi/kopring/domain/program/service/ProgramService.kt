package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.domain.program.repository.*
import org.springframework.stereotype.Service

@Service
class ProgramService(
    private val programRepository: ProgramRepository,
    private val programDetailRepository: ProgramDetailRepository,
    private val programLikeRepository: ProgramLikeRepository,
    private val programRecommendationRepository: ProgramRecommendationRepository,
    private val programRegistrationRepository: ProgramRegistrationRepository
)
