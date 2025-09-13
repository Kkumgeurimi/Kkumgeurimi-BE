package com.kkumgeurimi.kopring.domain.career.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "career_analysis")
class CareerAnalysis(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_analysis_id")
    val careerAnalysisId: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,

    @Column(name = "node1", columnDefinition = "JSONB")
    var node1: String? = null, // ToDo: 추후 JSON구조가 고정되면 객체로 저장하게 변경

    @Column(name = "node2", columnDefinition = "JSONB") 
    var node2: String? = null,

    @Column(name = "node3", columnDefinition = "JSONB")
    var node3: String? = null,

    @Column(name = "node4", columnDefinition = "JSONB")
    var node4: String? = null
) : BaseTime()
