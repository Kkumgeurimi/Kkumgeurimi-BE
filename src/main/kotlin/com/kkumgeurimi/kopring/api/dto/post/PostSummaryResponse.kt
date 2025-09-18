package com.kkumgeurimi.kopring.api.dto.post

import com.kkumgeurimi.kopring.domain.community.entity.Post
import java.time.LocalDateTime

// 게시글 목록 응답 DTO
data class PostSummaryResponse(
    val id: Long,
    val title: String?,
    val category: String,
    val authorNickname: String,
    val viewCount: Int,
    val likeCount: Int,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostSummaryResponse {
            return PostSummaryResponse(
                id = post.postId,
                title = post.title,
                category = post.category.name,
                authorNickname = post.author.nickname,
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                createdAt = post.createdAt
            )
        }
    }
}