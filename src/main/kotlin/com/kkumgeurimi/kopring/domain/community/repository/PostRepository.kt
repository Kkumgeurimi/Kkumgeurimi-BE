package com.kkumgeurimi.kopring.domain.community.repository

import com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse
import com.kkumgeurimi.kopring.domain.community.entity.Post
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostRepository : JpaRepository<Post, Long>{
    @Query("""
    SELECT p FROM Post p
    JOIN FETCH p.author
    LEFT JOIN FETCH p.comments c
    LEFT JOIN FETCH c.author
    WHERE p.postId = :postId
""")
    fun findPostWithComments(@Param("postId") postId: Long): Post?


    @Query("""
        SELECT new com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse(
            p.postId,
            p.title,
            p.category,
            p.author.nickname,
            p.viewCount,
            p.likeCount,
            COUNT(c),
            p.createdAt
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE (:schoolLevel IS NULL OR p.author.school = :schoolLevel)
        GROUP BY p.postId, p.title, p.category, p.author.nickname, p.viewCount, p.likeCount, p.createdAt
    """)
    fun findAllWithCommentCount(
        @Param("schoolLevel") schoolLevel: String?,
        pageable: Pageable
    ): Page<PostSummaryResponse>

    @Query("""
    SELECT new com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse(
        p.postId,
        p.title,
        p.category,
        p.author.nickname,
        p.viewCount,
        p.likeCount,
        COUNT(c),
        p.createdAt
    )
    FROM Post p
    LEFT JOIN p.comments c
    WHERE p.author = :author
    GROUP BY p.postId, p.title, p.category, p.author.nickname, p.viewCount, p.likeCount, p.createdAt
""")
    fun findByAuthorWithCommentCount(
        @Param("author") author: Student,
        pageable: Pageable
    ): Page<PostSummaryResponse>

    @Query("""
    SELECT new com.kkumgeurimi.kopring.api.dto.post.PostSummaryResponse(
        p.postId,
        p.title,
        p.category,
        p.author.nickname,
        p.viewCount,
        p.likeCount,
        COUNT(c),
        p.createdAt
    )
    FROM PostLike pl
    JOIN pl.post p
    JOIN p.author a
    LEFT JOIN p.comments c
    WHERE pl.student = :student
    GROUP BY p.postId, p.title, p.category, a.nickname, p.viewCount, p.likeCount, p.createdAt
""")
    fun findLikedPostsByStudent(
        @Param("student") student: Student,
        pageable: Pageable
    ): Page<PostSummaryResponse>
}