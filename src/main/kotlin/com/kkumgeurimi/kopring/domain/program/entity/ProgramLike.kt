package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "program_like")
class ProgramLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_like_id", unique = true, nullable = false)
    val programLikeId: Long,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "program_id", nullable = false)
    val program: Program,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    val student: Student
) : BaseTime()