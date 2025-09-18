package com.kkumgeurimi.kopring.domain.community.repository

import com.kkumgeurimi.kopring.domain.community.entity.Post
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostRepository : JpaRepository<Post, Long>{
    fun findByAuthor(author: Student, pageable: Pageable): Page<Post>
    @Query("""
    SELECT p FROM Post p
    JOIN FETCH p.author
    LEFT JOIN FETCH p.comments c
    LEFT JOIN FETCH c.author
    WHERE p.postId = :postId
""")
    fun findPostWithComments(@Param("postId") postId: Long): Post?
}