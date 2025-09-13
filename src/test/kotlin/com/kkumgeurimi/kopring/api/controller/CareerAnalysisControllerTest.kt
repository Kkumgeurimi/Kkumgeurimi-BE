package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.CareerBubbleMapResponse
import com.kkumgeurimi.kopring.api.dto.CareerNodeResponse
import com.kkumgeurimi.kopring.domain.career.service.CareerAnalysisService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(CareerAnalysisController::class)
class CareerAnalysisControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var careerAnalysisService: CareerAnalysisService

    @Test
    @DisplayName("진로버블맵 조회 - 성공")
    @WithMockUser
    fun getCareerMap_Success() {
        // given
        val mockBubbleMapResponse = CareerBubbleMapResponse(
            nodes = listOf(
                CareerNodeResponse(
                    nodeId = "node1",
                    nodeName = "기획",
                    programIds = listOf("PROG001", "PROG002")
                ),
                CareerNodeResponse(
                    nodeId = "node2", 
                    nodeName = "마케팅",
                    programIds = listOf("PROG003", "PROG004")
                )
            ),
            createdAt = "2024-01-15T10:30:00",
            modifiedAt = "2024-01-15T14:20:00"
        )

        whenever(careerAnalysisService.getCurrentStudentCareerBubbleMap()).thenReturn(mockBubbleMapResponse)

        // when & then
        mockMvc.perform(get("/careermap")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nodes").isArray)
            .andExpect(jsonPath("$.nodes[0].nodeId").value("node1"))
            .andExpect(jsonPath("$.nodes[0].nodeName").value("기획"))
            .andExpect(jsonPath("$.nodes[0].programIds[0]").value("PROG001"))
            .andExpect(jsonPath("$.nodes[0].programIds[1]").value("PROG002"))
            .andExpect(jsonPath("$.nodes[1].nodeId").value("node2"))
            .andExpect(jsonPath("$.nodes[1].nodeName").value("마케팅"))
            .andExpect(jsonPath("$.nodes[1].programIds[0]").value("PROG003"))
            .andExpect(jsonPath("$.nodes[1].programIds[1]").value("PROG004"))
            .andExpect(jsonPath("$.createdAt").value("2024-01-15T10:30:00"))
            .andExpect(jsonPath("$.modifiedAt").value("2024-01-15T14:20:00"))
    }

    @Test
    @DisplayName("진로버블맵 조회 - 분석 결과 없음")
    @WithMockUser
    fun getCareerMap_NoAnalysis() {
        // given
        whenever(careerAnalysisService.getCurrentStudentCareerBubbleMap()).thenReturn(null)

        // when & then
        mockMvc.perform(get("/careermap")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    @DisplayName("진로버블맵 조회 - 인증되지 않은 사용자")
    fun getCareerMap_Unauthorized() {
        // when & then
        mockMvc.perform(get("/careermap")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }

}

