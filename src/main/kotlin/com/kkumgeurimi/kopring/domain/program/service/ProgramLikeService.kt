package com.kkumgeurimi.kopring.domain.program.service

import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import com.kkumgeurimi.kopring.domain.program.repository.ProgramLikeRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProgramLikeService(
    private val programLikeRepository: ProgramLikeRepository,
    private val authService: AuthService,
    private val programQueryService: ProgramQueryService
) {

    fun likeProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = programQueryService.getProgramById(programId)

        val existingLike = programLikeRepository.findByProgramAndStudent(program, currentStudent)
        if (existingLike != null) {
            throw CustomException(ErrorCode.DUPLICATE_PROGRAM_LIKE)
        }

        val like = ProgramLike(
            programLikeId = 0,
            program = program,
            student = currentStudent
        )

        programLikeRepository.save(like)
    }

    fun unlikeProgram(programId: String) {
        val currentStudent = authService.getCurrentStudent()
        val program = programQueryService.getProgramById(programId)

        val like = programLikeRepository.findByProgramAndStudent(program, currentStudent)
            ?: throw CustomException(ErrorCode.ERROR)

        programLikeRepository.delete(like)
    }
}