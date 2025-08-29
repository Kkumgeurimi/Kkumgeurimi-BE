package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program_recommendation")
class ProgramRecommendation(
    @Column(name = "program_recommendation_id", unique = true, nullable = false)
    val programRecommendationId: String,
    
    @Column(name = "program_id", nullable = false)
    val programId: String,
    
    @Column(name = "student_id", nullable = false)
    val studentId: Long,
    
    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    val recommendationReason: String? = null
) : BaseTime()