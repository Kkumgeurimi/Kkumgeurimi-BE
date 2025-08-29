package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "program_registration")
class ProgramRegistration(
    @Id
    @Column(name = "program_registration_id", unique = true, nullable = false)
    val programRegistrationId: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    val program: Program,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,
    
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
