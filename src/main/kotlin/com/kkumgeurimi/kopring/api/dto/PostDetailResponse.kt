package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.community.entity.Post
import java.time.LocalDateTime

// 게시글 상세 응답 DTO
data class PostDetailResponse(
    val id: Long,
    val title: String?,
    val content: String,
    val category: String,
    val authorName: String,
    val viewCount: Int,
    val likeCount: Int,
    val createdAt: LocalDateTime?,
    val comments: List<CommentResponse>
) {
    companion object {
        fun from(post: Post): PostDetailResponse {
            return PostDetailResponse(
                id = post.postId,
                title = post.title,
                content = post.content,
                category = post.category.name,
                authorName = post.author.name,
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                createdAt = post.createdAt,
                comments = post.comments.map { CommentResponse.from(it) }
            )
        }
    }
}