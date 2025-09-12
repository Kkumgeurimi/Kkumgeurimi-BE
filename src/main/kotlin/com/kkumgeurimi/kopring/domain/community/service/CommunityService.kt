package com.kkumgeurimi.kopring.domain.community.service

import com.kkumgeurimi.kopring.api.dto.PostDetailResponse
import com.kkumgeurimi.kopring.api.dto.PostSummaryResponse
import com.kkumgeurimi.kopring.domain.community.entity.Comment
import com.kkumgeurimi.kopring.domain.community.entity.Post
import com.kkumgeurimi.kopring.domain.community.entity.PostCategory
import com.kkumgeurimi.kopring.domain.community.repository.CommentRepository
import com.kkumgeurimi.kopring.domain.community.repository.PostRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val authService: AuthService,
) {
    @Transactional
    fun createPost(title: String?, content: String, category: PostCategory): Post {
        val post = Post(
            title = title,
            content = content,
            category = category,
            author = authService.getCurrentStudent()
        )
        return postRepository.save(post)
    }

    @Transactional
    fun getPosts(page: Int, size: Int): Page<PostSummaryResponse> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findAll(pageable).map { PostSummaryResponse.from(it) }
    }

    @Transactional
    fun getPostDetail(postId: Long): PostDetailResponse {
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("게시글 없음") }
        // 조회수 증가 로직 (원하면 여기서 처리 가능)
        post.viewCount += 1
        return PostDetailResponse.from(post)
    }


    @Transactional
    fun deletePost(postId: Long) {
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("게시글 없음") }
        if (post.author.studentId != authService.getCurrentStudent().studentId) throw IllegalAccessException("삭제 권한 없음")
        postRepository.delete(post)
    }

    @Transactional
    fun addComment(postId: Long, content: String): Comment {
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("게시글 없음") }
        val comment = Comment(
            content = content,
            post = post,
            author = authService.getCurrentStudent()
        )
        return commentRepository.save(comment)
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow { IllegalArgumentException("댓글 없음") }
        if (comment.author.studentId != authService.getCurrentStudent().studentId) throw IllegalAccessException("삭제 권한 없음")
        commentRepository.delete(comment)
    }
}
