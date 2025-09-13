package com.kkumgeurimi.kopring.domain.community.repository

import com.kkumgeurimi.kopring.domain.community.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>
