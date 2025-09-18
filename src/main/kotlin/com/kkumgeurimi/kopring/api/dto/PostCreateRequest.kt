package com.kkumgeurimi.kopring.api.dto

import com.kkumgeurimi.kopring.domain.community.entity.PostCategory

data class PostCreateRequest(
    val title: String? = null,
    val content: String,
    val category: PostCategory
)
