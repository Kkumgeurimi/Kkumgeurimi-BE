package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program_detail")
class ProgramDetail(
    @Column(name = "program_detail_id", unique = true, nullable = false)
    val programDetailId: String,
    
    @Column(name = "program_id", nullable = false)
    val programId: String,
    
    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,
    
    @Column(name = "capacity", length = 100)
    val capacity: String? = null,
    
    @Column(name = "available_hours", length = 100)
    val availableHours: String? = null,
    
    @Column(name = "target_school_type", length = 255)
    val targetSchoolType: String? = null,
    
    @Column(name = "venue", length = 500)
    val venue: String? = null,
    
    @Column(name = "level_info", columnDefinition = "TEXT")
    val levelInfo: String? = null,
    
    @Column(name = "caution", columnDefinition = "TEXT")
    val caution: String? = null
) : BaseTime()