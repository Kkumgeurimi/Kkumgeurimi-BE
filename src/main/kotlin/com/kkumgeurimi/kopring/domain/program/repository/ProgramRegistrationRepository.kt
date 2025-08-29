package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRegistrationRepository : JpaRepository<ProgramRegistration, Long>