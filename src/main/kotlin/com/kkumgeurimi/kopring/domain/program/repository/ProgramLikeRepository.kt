package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProgramLikeRepository : JpaRepository<ProgramLike, Long> {
    fun existsByProgramAndStudent(program: Program, student: Student): Boolean
    fun countByProgram(program: Program): Long
    fun findByProgramAndStudent(program: Program, student: Student): ProgramLike?
    @Query("SELECT pl FROM ProgramLike pl JOIN FETCH pl.program WHERE pl.student = :student")
    fun findByStudentWithProgram(student: Student): List<ProgramLike>
}