package com.kkumgeurimi.kopring.domain.community.repository

import com.kkumgeurimi.kopring.domain.community.entity.PostLike
import com.kkumgeurimi.kopring.domain.community.entity.Post
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun existsByPostAndStudent(post: Post, student: Student): Boolean
}
