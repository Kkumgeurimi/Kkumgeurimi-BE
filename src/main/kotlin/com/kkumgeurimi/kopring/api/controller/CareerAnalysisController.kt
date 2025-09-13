package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.career.CareerAnalysisResponse
import com.kkumgeurimi.kopring.domain.career.service.CareerAnalysisService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "CareerAnalysis")
@RestController
class CareerAnalysisController(
    private val careerAnalysisService: CareerAnalysisService
) {

    @Operation(summary = "진로 분석 결과 조회", description = "현재 학생의 진로 분석 결과를 반환합니다.")
    @GetMapping("/careermap")
    fun getCareerAnalysis(): CareerAnalysisResponse? {
        return careerAnalysisService.getCurrentStudentCareerAnalysis()
    }

}
