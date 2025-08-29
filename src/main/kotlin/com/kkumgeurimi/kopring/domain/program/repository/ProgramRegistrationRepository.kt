package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRegistrationRepository : JpaRepository<ProgramRegistration, Long> {
    fun countByProgram(program: Program): Long
}