package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramLikeRepository : JpaRepository<ProgramLike, Long> {
    fun existsByProgramAndStudent(program: Program, student: Student): Boolean
    fun countByProgram(program: Program): Long
    fun findByProgramAndStudent(program: Program, student: Student): ProgramLike?
}