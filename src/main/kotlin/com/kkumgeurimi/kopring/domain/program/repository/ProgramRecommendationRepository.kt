package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.ProgramRecommendation
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRecommendationRepository : JpaRepository<ProgramRecommendation, Long>