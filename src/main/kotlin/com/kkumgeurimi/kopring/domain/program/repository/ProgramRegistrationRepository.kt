package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRegistrationRepository : JpaRepository<ProgramRegistration, Long> {
    fun findByProgramAndStudent(program: Program, student: Student): ProgramRegistration?
    fun countByProgram(program: Program): Long
    fun findByStudentOrderByCreatedAtDesc(student: Student): List<ProgramRegistration>
}