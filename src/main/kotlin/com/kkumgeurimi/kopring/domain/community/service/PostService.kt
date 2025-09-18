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
        val pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findAllWithCommentCount(currentStudentLevel, pageable)
    }

    @Transactional // 조회수 증가 때문
    fun getPostDetail(postId: Long): PostDetailResponse {
        val post = postRepository.findPostWithComments(postId)
            ?: throw CustomException(ErrorCode.POST_NOT_FOUND)
        val currentStudent = authService.getCurrentStudent()
        val currentStudentLevel = currentStudent.getSchoolLevel()
        if (currentStudentLevel != null && post.author.getSchoolLevel() != currentStudentLevel) {
            throw CustomException(ErrorCode.UNAUTHORIZED_ACCESS)
        }
        post.viewCount += 1 // TODO: 추후 리팩토링
        return PostDetailResponse.from(post, currentStudent.studentId)
    }

    @Transactional
    fun deletePost(postId: Long) {
        val post = postRepository.findById(postId).orElseThrow { CustomException(ErrorCode.POST_NOT_FOUND) }
        if (post.author.studentId != authService.getCurrentStudent().studentId) throw IllegalAccessException("삭제 권한 없음")
        postRepository.delete(post)
    }

    @Transactional
    fun getMyPosts(page: Int, size: Int): Page<PostSummaryResponse> {
        val currentStudent = authService.getCurrentStudent()
        val pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findByAuthorWithCommentCount(currentStudent, pageable)
    }

    @Transactional
    fun getMyLikedPosts(page: Int, size: Int): Page<PostSummaryResponse> {
        val currentStudent = authService.getCurrentStudent()
        val pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findLikedPostsByStudent(currentStudent, pageable)
    }
}