package com.kkumgeurimi.kopring.api.dto.career

import com.kkumgeurimi.kopring.domain.career.entity.CareerAnalysis
import java.time.format.DateTimeFormatter

data class CareerAnalysisResponse(
    val careerAnalysisId: Long,
    val studentId: Long,
    val node1: String?,
    val node2: String?,
    val node3: String?,
    val node4: String?,
    val createdAt: String?,
    val modifiedAt: String?
) {
    companion object {
        fun from(careerAnalysis: CareerAnalysis): CareerAnalysisResponse {
            return CareerAnalysisResponse(
                careerAnalysisId = careerAnalysis.careerAnalysisId,
                studentId = careerAnalysis.student.studentId,
                node1 = careerAnalysis.node1,
                node2 = careerAnalysis.node2,
                node3 = careerAnalysis.node3,
                node4 = careerAnalysis.node4,
                createdAt = careerAnalysis.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                modifiedAt = careerAnalysis.modifiedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        }
    }
}
