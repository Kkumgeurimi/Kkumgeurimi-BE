package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "program_recommendation")
class ProgramRecommendation(
    @Id
    @Column(name = "program_recommendation_id", unique = true, nullable = false)
    val programRecommendationId: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    val program: Program,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,
    
    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    val recommendationReason: String? = null
) : BaseTime()