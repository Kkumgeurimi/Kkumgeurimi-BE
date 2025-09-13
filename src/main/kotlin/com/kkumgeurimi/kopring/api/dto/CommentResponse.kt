package com.kkumgeurimi.kopring.api.dto

import java.time.LocalDateTime

// 댓글 응답 DTO
data class CommentResponse(
    val id: Long,
    val content: String,
    val authorGrade: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(comment: com.kkumgeurimi.kopring.domain.community.entity.Comment): CommentResponse {
            val authorGrade = comment.author.calculateGrade() ?: "익명"
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                authorGrade = authorGrade,
                createdAt = comment.createdAt
            )
        }
    }
}
