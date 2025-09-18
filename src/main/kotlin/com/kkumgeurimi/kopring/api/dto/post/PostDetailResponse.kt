package com.kkumgeurimi.kopring.api.dto.post

import com.kkumgeurimi.kopring.domain.community.entity.Post
import java.time.LocalDateTime

data class PostDetailResponse(
    val id: Long,
    val title: String?,
    val content: String,
    val category: String,
    val authorNickname: String,
    val viewCount: Int,
    val likeCount: Int,
    val createdAt: LocalDateTime?,
    val comments: List<CommentResponse>
) {
    companion object {
        fun from(post: Post, currentStudentId: Long?): PostDetailResponse {
            return PostDetailResponse(
                id = post.postId,
                title = post.title,
                content = post.content,
                category = post.category.name,
                authorNickname = post.author.nickname,
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                createdAt = post.createdAt,
                comments = post.comments.map {
                    CommentResponse(
                        id = it.id,
                        content = it.content,
                        authorNickname = it.author.nickname,
                        createdAt = it.createdAt,
                        isAuthor = currentStudentId != null && it.author.studentId == currentStudentId
                    )
                }
            )
        }
    }
}
