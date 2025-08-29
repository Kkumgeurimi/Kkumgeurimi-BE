package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program_like")
class ProgramLike(
    @Column(name = "program_like_id", unique = true, nullable = false)
    val programLikeId: String,
    
    @Column(name = "program_id", nullable = false)
    val programId: String,
    
    @Column(name = "student_id", nullable = false)
    val studentId: Long
) : BaseTime()