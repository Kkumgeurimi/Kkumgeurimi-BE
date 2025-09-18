package com.kkumgeurimi.kopring.api.dto.post

import com.kkumgeurimi.kopring.domain.community.entity.PostCategory
import java.time.LocalDateTime

// 게시글 목록 응답 DTO
data class PostSummaryResponse(
    val id: Long,
    val title: String?,
    val category: String,
    val authorNickname: String,
    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Long,
    val createdAt: LocalDateTime?
) {
    constructor(
        id: Long,
        title: String,
        category: PostCategory,
        authorNickname: String,
        viewCount: Int,
        likeCount: Int,
        commentCount: Long,
        createdAt: LocalDateTime
    ) : this(
        id,
        title,
        category.name,
        authorNickname,
        viewCount,
        likeCount,
        commentCount,
        createdAt
    )
}