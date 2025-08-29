package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramLikeRepository : JpaRepository<ProgramLike, Long> {
    fun countByProgram(program: Program): Long
}