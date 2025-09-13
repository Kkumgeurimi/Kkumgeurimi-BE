package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "program_registration")
class ProgramRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_registration_id", unique = true, nullable = false)
    val programRegistrationId: Long,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "program_id", nullable = false)
    val program: Program,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    val student: Student,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    val registrationStatus: RegistrationStatus = RegistrationStatus.REGISTERED,
    
    @Column(name = "review_score", length = 10)
    val reviewScore: String? = null,
    
    @Column(name = "review_message", columnDefinition = "TEXT")
    val reviewMessage: String? = null
) : BaseTime() {
    
    fun isProgramCompleted(): Boolean {
        return program.endDate?.let { endDate ->
            endDate.isBefore(LocalDate.now())
        } ?: false
    }
}

enum class RegistrationStatus {
    REGISTERED, // 신청
    COMPLETED   // 완료(endDate 지남)
}
