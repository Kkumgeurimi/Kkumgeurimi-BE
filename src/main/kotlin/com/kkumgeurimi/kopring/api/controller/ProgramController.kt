package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.domain.program.service.ProgramService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/programs")
class ProgramController(
    private val programService: ProgramService
)
