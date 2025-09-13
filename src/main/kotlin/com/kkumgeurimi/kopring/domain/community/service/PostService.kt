package com.kkumgeurimi.kopring.domain.community.service

import com.kkumgeurimi.kopring.api.dto.post.PostDetailResponse
import com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.community.entity.Post
import com.kkumgeurimi.kopring.domain.community.entity.PostCategory
import com.kkumgeurimi.kopring.domain.community.repository.PostRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
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
        val currentStudent = authService.getCurrentStudent()
        val currentStudentLevel = currentStudent.getSchoolLevel()
        
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        
        val allPosts = postRepository.findAll(pageable)
        
        return if (currentStudentLevel != null) {
            val filteredPosts = allPosts.content
                .filter { post -> post.author.getSchoolLevel() == currentStudentLevel }
                .map { post -> 
                    PostSummaryResponse(
                        id = post.postId,
                        title = post.title,
                        category = post.category.name,
                        authorGrade = post.author.calculateGrade() ?: "익명",
                        viewCount = post.viewCount,
                        likeCount = post.likeCount,
                        createdAt = post.createdAt
                    )
                }
            PageImpl(filteredPosts, pageable, filteredPosts.size.toLong())
        } else {
            allPosts.map { PostSummaryResponse.from(it) }
        }
    }

    @Transactional
    fun getPostDetail(postId: Long): PostDetailResponse {
        val post = postRepository.findById(postId).orElseThrow { CustomException(ErrorCode.POST_NOT_FOUND) }
        val currentStudent = authService.getCurrentStudent()
        val currentStudentLevel = currentStudent.getSchoolLevel()
        
        if (currentStudentLevel != null && post.author.getSchoolLevel() != currentStudentLevel) {
            throw CustomException(ErrorCode.ERROR) //todo: 추가
        }
        
        post.viewCount += 1
        
        return PostDetailResponse(
            id = post.postId,
            title = post.title,
            content = post.content,
            category = post.category.name,
            authorGrade = post.author.calculateGrade() ?: "익명",
            viewCount = post.viewCount,
            likeCount = post.likeCount,
            createdAt = post.createdAt,
            comments = post.comments.map { 
                com.kkumgeurimi.kopring.api.dto.CommentResponse(
                    id = it.id,
                    content = it.content,
                    authorGrade = it.author.calculateGrade() ?: "익명",
                    createdAt = it.createdAt
                )
            }
        )
    }

    @Transactional
    fun deletePost(postId: Long) {
        val post = postRepository.findById(postId).orElseThrow { CustomException(ErrorCode.POST_NOT_FOUND) }
        if (post.author.studentId != authService.getCurrentStudent().studentId) throw IllegalAccessException("삭제 권한 없음")
        postRepository.delete(post)
    }
}