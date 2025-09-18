package com.kkumgeurimi.kopring.api.dto.post

import com.kkumgeurimi.kopring.domain.community.entity.Comment
import java.time.LocalDateTime

// 댓글 응답 DTO
data class CommentResponse(
    val id: Long,
    val content: String,
    val authorNickname: String,
    val createdAt: LocalDateTime?,
    val isAuthor: Boolean
) {
    companion object {
        fun from(comment: Comment, authorNickname: String, isAuthor: Boolean): CommentResponse {
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                authorNickname = authorNickname,
                createdAt = comment.createdAt,
                isAuthor = isAuthor
            )
        }
    }
}
