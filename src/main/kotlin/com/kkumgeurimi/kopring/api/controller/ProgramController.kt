package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.domain.program.service.ProgramService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Program")
@RestController
@RequestMapping("/programs")
class ProgramController(
    private val programService: ProgramService
)
