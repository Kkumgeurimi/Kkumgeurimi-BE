package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.program.entity.Program
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "프로그램 응답")
data class ProgramResponse(
    @Schema(description = "프로그램 ID")
    val programId: String,
    
    @Schema(description = "프로그램 제목")
    val programTitle: String,
    
    @Schema(description = "제공자")
    val provider: String?,
    
    @Schema(description = "대상")
    val targetAudience: String?,
    
    @Schema(description = "체험유형")
    val programType: String?,
    
    @Schema(description = "시작 날짜")
    val startDate: String?,
    
    @Schema(description = "종료 날짜")
    val endDate: String?,
    
    @Schema(description = "관련 전공")
    val relatedMajor: String?,
    
    @Schema(description = "가격")
    val price: String?,
    
    @Schema(description = "이미지 URL")
    val imageUrl: String?,
    
    @Schema(description = "지원 지역")
    val eligibleRegion: String?,
    
    @Schema(description = "장소 지역")
    val venueRegion: String?,
    
    @Schema(description = "운영 주기")
    val operateCycle: String?,
    
    @Schema(description = "관심 카테고리 ID")
    val interestCategoryId: Int?,
    
    @Schema(description = "관심 텍스트")
    val interestText: String?,
    
    @Schema(description = "좋아요 수")
    val likeCount: Long = 0,
    
    @Schema(description = "등록자 수")
    val registrationCount: Long = 0,
    
    @Schema(description = "생성 시간")
    val createdAt: LocalDateTime?,
    
    @Schema(description = "수정 시간")
    val modifiedAt: LocalDateTime?
) {
    companion object {
        fun from(program: Program, likeCount: Long = 0, registrationCount: Long = 0): ProgramResponse {
            return ProgramResponse(
                programId = program.programId,
                programTitle = program.programTitle,
                provider = program.provider,
                targetAudience = program.targetAudience,
                programType = program.programType,
                startDate = program.startDate,
                endDate = program.endDate,
                relatedMajor = program.relatedMajor,
                price = program.price,
                imageUrl = program.imageUrl,
                eligibleRegion = program.eligibleRegion,
                venueRegion = program.venueRegion,
                operateCycle = program.operateCycle,
                interestCategoryId = program.interestCategoryId,
                interestText = program.interestText,
                likeCount = likeCount,
                registrationCount = registrationCount,
                createdAt = program.createdAt,
                modifiedAt = program.modifiedAt
            )
        }
    }
}
