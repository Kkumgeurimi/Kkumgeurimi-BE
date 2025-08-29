package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program")
class Program(
    @Column(name = "program_id", unique = true, nullable = false)
    val programId: String,
    
    @Column(name = "site", length = 255)
    val site: String? = null,
    
    @Column(name = "provider", length = 255)
    val provider: String? = null,
    
    @Column(name = "objective", columnDefinition = "TEXT")
    val objective: String? = null,
    
    @Column(name = "target_audience", length = 500)
    val targetAudience: String? = null,
    
    @Column(name = "program_type", length = 100)
    val programType: String? = null,
    
    @Column(name = "online_type", length = 100)
    val onlineType: String? = null,
    
    @Column(name = "start_date", length = 50)
    val startDate: String? = null,
    
    @Column(name = "end_date", length = 50)
    val endDate: String? = null,
    
    @Column(name = "recruit_start", length = 50)
    val recruitStart: String? = null,
    
    @Column(name = "recruit_end", length = 50)
    val recruitEnd: String? = null,
    
    @Column(name = "price", length = 100)
    val price: String? = null,
    
    @Column(name = "inquiry_url", length = 1000)
    val inquiryUrl: String? = null,
    
    @Column(name = "application_region", length = 255)
    val applicationRegion: String? = null,
    
    @Column(name = "operation_region", length = 255)
    val operationRegion: String? = null,
    
    @Column(name = "operation_cycle", length = 100)
    val operationCycle: String? = null,
    
    @Column(name = "field_category", length = 255)
    val fieldCategory: String? = null
) : BaseTime()