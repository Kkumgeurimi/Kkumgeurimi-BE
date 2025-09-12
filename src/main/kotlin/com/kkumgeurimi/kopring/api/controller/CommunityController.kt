package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.CommentRequest
import com.kkumgeurimi.kopring.api.dto.PostDetailResponse
import com.kkumgeurimi.kopring.api.dto.PostSummaryResponse
import com.kkumgeurimi.kopring.domain.community.entity.PostCategory
import com.kkumgeurimi.kopring.domain.community.service.CommunityService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Community")
@RestController
class CommunityController(
    private val communityService: CommunityService
) {
    @PostMapping("/posts")
    fun createPost(
        @RequestParam(required = false) title: String?,
        @RequestParam content: String,
        @RequestParam category: PostCategory
    ): ResponseEntity<Void> {
        communityService.createPost(title, content, category)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/posts")
    fun getPosts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<PostSummaryResponse> {
        return communityService.getPosts(page, size)
    }

    @GetMapping("/posts/{postId}")
    fun getPostDetail(
        @PathVariable postId: Long
    ): PostDetailResponse {
        return communityService.getPostDetail(postId)
    }

    @DeleteMapping("/posts/{postId}")
    fun deletePost(
        @PathVariable postId: Long
    ): ResponseEntity<Void> {
        communityService.deletePost(postId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/posts/{postId}/comments")
    fun addComment(
        @PathVariable postId: Long,
        @RequestBody request: CommentRequest
    ): ResponseEntity<Void> {
        communityService.addComment(postId, request.content)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long
    ): ResponseEntity<Void> {
        communityService.deleteComment(commentId)
        return ResponseEntity.ok().build()
    }
}
