package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.CareerMapResponse
import com.kkumgeurimi.kopring.domain.career.service.CareerService
import com.kkumgeurimi.kopring.domain.student.entity.Student
import com.kkumgeurimi.kopring.domain.student.service.AuthService
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

@WebMvcTest(CareerController::class)
class CareerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var careerService: CareerService

    @MockitoBean
    private lateinit var authService: AuthService

    @Test
    @DisplayName("진로버블맵 조회 - 성공")
    @WithMockUser
    fun getCareerMap_Success() {
        // given
        val mockStudent = Student(
            email = "test@example.com",
            name = "테스트 학생",
            password = "password123"
        )
        val mockCareerMapResponse = listOf(
            CareerMapResponse(
                programId = "program1",
                title = "기획 프로그램",
                description = "기획 관련 프로그램",
                interestCategory = 0,
                interestCategoryLabel = "인문·사회과학 연구직"
            ),
            CareerMapResponse(
                programId = "program2",
                title = "마케팅 프로그램",
                description = "마케팅 관련 프로그램",
                interestCategory = 19,
                interestCategoryLabel = "영업·판매직"
            )
        )

        whenever(authService.getCurrentStudent()).thenReturn(mockStudent)
        whenever(careerService.getCurrentStudentCareerMap()).thenReturn(mockCareerMapResponse)

        // when & then
        mockMvc.perform(get("/careermap")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].programId").value("program1"))
            .andExpect(jsonPath("$[0].title").value("기획 프로그램"))
            .andExpect(jsonPath("$[0].description").value("기획 관련 프로그램"))
            .andExpect(jsonPath("$[0].interestCategory").value(0))
            .andExpect(jsonPath("$[0].interestCategoryLabel").value("인문·사회과학 연구직"))
            .andExpect(jsonPath("$[1].programId").value("program2"))
            .andExpect(jsonPath("$[1].title").value("마케팅 프로그램"))
            .andExpect(jsonPath("$[1].description").value("마케팅 관련 프로그램"))
            .andExpect(jsonPath("$[1].interestCategory").value(19))
            .andExpect(jsonPath("$[1].interestCategoryLabel").value("영업·판매직"))
    }

    @Test
    @DisplayName("진로버블맵 조회 - 빈 리스트")
    @WithMockUser
    fun getCareerMap_EmptyList() {
        // given
        val mockStudent = Student(
            email = "test@example.com",
            name = "테스트 학생",
            password = "password123"
        )

        whenever(authService.getCurrentStudent()).thenReturn(mockStudent)
        whenever(careerService.getCurrentStudentCareerMap()).thenReturn(emptyList())

        // when & then
        mockMvc.perform(get("/careermap")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
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
