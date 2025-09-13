package com.kkumgeurimi.kopring.domain.community.service

import com.kkumgeurimi.kopring.api.exception.CustomException
import com.kkumgeurimi.kopring.api.exception.ErrorCode
import com.kkumgeurimi.kopring.domain.community.entity.Comment
import com.kkumgeurimi.kopring.domain.community.repository.CommentRepository
import com.kkumgeurimi.kopring.domain.community.repository.PostRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val authService: AuthService,
) {
    @Transactional
    fun addComment(postId: Long, content: String): Comment {
        val post = postRepository.findById(postId).orElseThrow { CustomException(ErrorCode.POST_NOT_FOUND) }
        val currentStudent = authService.getCurrentStudent()
        val currentStudentLevel = currentStudent.getSchoolLevel()
        
        if (currentStudentLevel != null && post.author.getSchoolLevel() != currentStudentLevel) {
            throw CustomException(ErrorCode.UNAUTHORIZED_ACCESS)
        }
        
        val comment = Comment(
            content = content,
            post = post,
            author = currentStudent
        )
        return commentRepository.save(comment)
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow { CustomException(ErrorCode.COMMENT_NOT_FOUND)  }
        if (comment.author.studentId != authService.getCurrentStudent().studentId)
            throw CustomException(ErrorCode.UNAUTHORIZED_REQUEST)
        commentRepository.delete(comment)
    }
}