package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.CareerMapResponse
import com.kkumgeurimi.kopring.domain.career.service.CareerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Career")
@RestController
class CareerController(
    private val careerService: CareerService
) {

    @Operation(summary = "진로버블맵 조회")
    @GetMapping("/careermap")
    fun getCareerMap(): List<CareerMapResponse> {
        return careerService.getCurrentStudentCareerMap()
    }

}
