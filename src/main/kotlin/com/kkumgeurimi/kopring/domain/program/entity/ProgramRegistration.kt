package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "program_registration")
class ProgramRegistration(
    @Column(name = "program_registration_id", unique = true, nullable = false)
    val programRegistrationId: String,
    
    @Column(name = "program_id", nullable = false)
    val programId: String,
    
    @Column(name = "student_id", nullable = false)
    val studentId: Long,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    val registrationStatus: RegistrationStatus,
    
    @Column(name = "review_score", length = 10)
    val reviewScore: String? = null,
    
    @Column(name = "review_message", columnDefinition = "TEXT")
    val reviewMessage: String? = null
) : BaseTime()

enum class RegistrationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED
}
