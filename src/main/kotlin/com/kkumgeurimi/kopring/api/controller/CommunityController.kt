package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.api.dto.PostCreateRequest
import com.kkumgeurimi.kopring.api.dto.post.CommentRequest
import com.kkumgeurimi.kopring.api.dto.post.PostDetailResponse
import com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse
import com.kkumgeurimi.kopring.domain.community.service.PostService
import com.kkumgeurimi.kopring.domain.community.service.CommentService
import com.kkumgeurimi.kopring.domain.community.service.PostLikeService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Community")
@RestController
class CommunityController(
    private val postService: PostService,
    private val commentService: CommentService,
    private val postLikeService: PostLikeService
) {
    @PostMapping("/posts")
    fun createPost(
        @RequestBody request: PostCreateRequest
    ): ResponseEntity<Void> {
        postService.createPost(request.title, request.content, request.category)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/posts")
    fun getPosts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<PostSummaryResponse> {
        return postService.getPosts(page, size)
    }

    @GetMapping("/posts/{postId}")
    fun getPostDetail(
        @PathVariable postId: Long
    ): PostDetailResponse {
        return postService.getPostDetail(postId)
    }

    @DeleteMapping("/posts/{postId}")
    fun deletePost(
        @PathVariable postId: Long
    ): ResponseEntity<Void> {
        postService.deletePost(postId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/posts/{postId}/comments")
    fun addComment(
        @PathVariable postId: Long,
        @RequestBody request: CommentRequest
    ): ResponseEntity<Void> {
        commentService.addComment(postId, request.content)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long
    ): ResponseEntity<Void> {
        commentService.deleteComment(commentId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/posts/{postId}/like")
    fun createPostLike(
        @PathVariable postId: Long
    ): ResponseEntity<Void> {
        postLikeService.createPostLike(postId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
