package com.kkumgeurimi.kopring.domain.community.repository

import com.kkumgeurimi.kopring.domain.community.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
