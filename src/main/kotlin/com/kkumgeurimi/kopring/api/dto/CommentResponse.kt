package com.kkumgeurimi.kopring.api.dto

import java.time.LocalDateTime

// 댓글 응답 DTO
data class CommentResponse(
    val id: Long,
    val content: String,
    val authorName: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(comment: com.kkumgeurimi.kopring.domain.community.entity.Comment): CommentResponse {
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                authorName = comment.author.name,
                createdAt = comment.createdAt
            )
        }
    }
}
