package com.kkumgeurimi.kopring.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(description = "페이지네이션 응답")
data class PageResponse<T>(
    @Schema(description = "데이터 목록")
    val content: List<T>,
    
    @Schema(description = "현재 페이지 번호")
    val pageNumber: Int,
    
    @Schema(description = "페이지 크기")
    val pageSize: Int,
    
    @Schema(description = "전체 요소 수")
    val totalElements: Long,
    
    @Schema(description = "전체 페이지 수")
    val totalPages: Int,
    
    @Schema(description = "다음 페이지 존재 여부")
    val hasNext: Boolean,
    
    @Schema(description = "이전 페이지 존재 여부")
    val hasPrevious: Boolean
) {
    companion object {
        fun <T> from(page: Page<T>): PageResponse<T> {
            return PageResponse(
                content = page.content,
                pageNumber = page.number,
                pageSize = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                hasNext = page.hasNext(),
                hasPrevious = page.hasPrevious()
            )
        }
    }
}
