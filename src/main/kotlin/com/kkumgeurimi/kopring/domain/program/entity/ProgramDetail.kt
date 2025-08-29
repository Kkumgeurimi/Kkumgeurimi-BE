package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program_detail")
class ProgramDetail(
    @Id
    @Column(name = "program_detail_id", unique = true, nullable = false)
    val programDetailId: String,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    val program: Program,
    
    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,
    
    @Column(name = "required_hours", length = 100)
    val requiredHours: String? = null,
    
    @Column(name = "avail_hours", length = 100)
    val availHours: String? = null,
    
    @Column(name = "capacity")
    val capacity: Int? = null,
    
    @Column(name = "target_school_type", length = 255)
    val targetSchoolType: String? = null,
    
    @Column(name = "level_info", columnDefinition = "TEXT")
    val levelInfo: String? = null,

) : BaseTime()