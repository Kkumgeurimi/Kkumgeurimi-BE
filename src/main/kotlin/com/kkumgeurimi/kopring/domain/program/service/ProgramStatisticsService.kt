package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.repository.ProgramLikeRepository
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProgramStatisticsService(
    private val programLikeRepository: ProgramLikeRepository,
    private val programRegistrationRepository: ProgramRegistrationRepository,
    private val authService: AuthService
) {
    fun getProgramStats(program: Program): ProgramStats {
        val likeCount = programLikeRepository.countByProgram(program)
        val registrationCount = programRegistrationRepository.countByProgram(program)
        val currentStudentOrNull = authService.getCurrentStudentOrNull()
        val likedByMe = currentStudentOrNull?.let {
            programLikeRepository.existsByProgramAndStudent(program, it)
        } ?: false
        val registeredByMe = currentStudentOrNull?.let {
            programRegistrationRepository.existsByProgramAndStudent(program, it)
        } ?: false

        return ProgramStats(likeCount, registrationCount, likedByMe, registeredByMe)
    }

    data class ProgramStats(
        val likeCount: Long,
        val registrationCount: Long,
        val likedByMe: Boolean,
        val registeredByMe: Boolean
    )
}
