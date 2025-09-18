package com.kkumgeurimi.kopring.domain.community.service

import com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse
import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.community.entity.PostLike
import com.kkumgeurimi.kopring.domain.community.repository.PostLikeRepository
import com.kkumgeurimi.kopring.domain.community.repository.PostRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PostLikeService(
    private val postRepository: PostRepository,
    private val authService: AuthService,
    private val postLikeRepository: PostLikeRepository,
) {
    @Transactional
    fun createPostLike(postId: Long) {
        val post = postRepository.findById(postId)
            .orElseThrow { CustomException(ErrorCode.POST_NOT_FOUND) }
        val student = authService.getCurrentStudent()
        // 이미 좋아요 눌렀는지 확인
        if (postLikeRepository.existsByPostAndStudent(post, student)) {
            throw CustomException(ErrorCode.DUPLICATE_POST_LIKE)
        }
        // 좋아요 저장
        val postLike = PostLike(post = post, student = student)
        postLikeRepository.save(postLike)
        // 카운트 증가
        post.likeCount += 1
    }
}
