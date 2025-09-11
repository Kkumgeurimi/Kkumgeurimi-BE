package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramDetailRepository : JpaRepository<ProgramLike, Long> {
}